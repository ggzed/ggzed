<template>
  <div class="app-container">
    <div class="content">
      <el-row
          v-for="(apiInfo, index) in apiInfoList"
          :key="index"
          style="margin-bottom: 20px"
      >
        <el-col :span="24">
          <!-- 信息条 -->
          <div class="info-api" @click="toggleHiddenBox(index)">
            <div id="methodDisplay" class="info-method">
              {{ apiInfo.method }}
            </div>
            <div class="info-url">
              {{ apiInfo.endpoint }}
            </div>
            <el-icon
                :style="{
              transform: apiStatusMap.isRotated[index]
                ? 'rotate(180deg)'
                : 'rotate(0)',
            }"
                class="transparent-icon"
            >
              <ArrowDown/>
            </el-icon>
          </div>

          <!-- 隐藏表单 -->
          <transition enter-active-class="animate__animated animate__jackInTheBox" name="fade">
            <div v-if="apiStatusMap.isHiddenBoxVisible[index]" class="hidden-box">
              <el-card class="inner-card">
                <ul>
                  <!-- 路径参数 -->
                  <li>
                    <span>路径参数</span>
                    <div class="input-box">
                      <el-input
                          v-for="(value, i) in inputValuesMap[index].pathParams"
                          :key="i"
                          v-model="inputValuesMap[index].pathParams[i]"
                          placeholder="请输入参数"
                          style="margin-bottom: 5px"
                      />
                    </div>
                  </li>

                  <!-- 请求参数 -->
                  <li>
                    <span>请求参数</span>
                    <el-tooltip
                        class="item"
                        content="GET, POST, DELETE, PUT请求会自动转换，请填写JSON字符串即可"
                        effect="dark"
                        placement="top"
                    >
                      <el-icon size="20">
                        <QuestionFilled/>
                      </el-icon>
                    </el-tooltip>
                    <el-input
                        v-model="inputValuesMap[index].params"
                        :autosize="{ minRows: 1, maxRows: 10 }"
                        placeholder="请输入有效的对象或JSON格式"
                        style="width: 85%"
                        type="textarea"
                    ></el-input>
                  </li>

                  <!-- 响应结果 -->
                  <li>
                    <span>响应结果</span>
                    <div class="result-info">{{ apiInfo.result }}</div>
                  </li>

                  <!-- 提交按钮 -->
                  <li>
                    <div></div>
                    <el-button
                        :loading="apiStatusMap.isLoading[index]"
                        block
                        type="primary"
                        @click="onSubmit(index)"
                    >
                      Send
                    </el-button>
                  </li>
                </ul>
              </el-card>
            </div>
          </transition>
        </el-col>
      </el-row>
    </div>
  </div>
</template>


<script lang="ts" setup>
import {onMounted, reactive} from "vue";
import {DynamicLogicSwitchListAPI} from "@/api/demo/dynamic-logic-switch";
import {toInteger} from "lodash";

// 1. API 相关
interface ApiInfo {
  method: string;
  endpoint: string;
  result?: string;
}

const apiInfoList: ApiInfo[] = Object.entries(DynamicLogicSwitchListAPI)
    .filter(([, api]) => api.path)
    .map(([, api]) => {

      // 通过查看 request 函数的实现来获取实际的请求方法
      const requestImpl = api.request.toString();
      const methodMatch = requestImpl.match(/method:\s*['"](\w+)['"]/);
      const method = methodMatch ? methodMatch[1] : 'GET';

      return {
        method,
        endpoint: api.path,
      };
    });

// 2. 输入值管理
interface InputValues {
  pathParams: string[];
  params: string;
}

const inputValuesMap = reactive<Record<number, InputValues>>({});

const initializeInputValues = (apiInfo: ApiInfo, index: number): void => {
  const matches = apiInfo.endpoint.match(/{[^}]+}/g);
  inputValuesMap[index] = {
    pathParams: matches ? matches.map(() => "") : [],
    params: "",
  };
};

// 3. UI 状态管理
interface ApiStatusMap {
  isHiddenBoxVisible: Record<number, boolean>;
  isRotated: Record<number, boolean>;
  isLoading: Record<number, boolean>;
}

const apiStatusMap = reactive<ApiStatusMap>({
  isHiddenBoxVisible: {},
  isRotated: {},
  isLoading: {},
});

const toggleHiddenBox = (index: number): void => {
  Object.keys(apiStatusMap.isHiddenBoxVisible).forEach((key) => {
    const idx = parseInt(key);
    if (idx === index) return;
    apiStatusMap.isHiddenBoxVisible[idx] = false;
    apiStatusMap.isRotated[idx] = false;
  });
  apiStatusMap.isHiddenBoxVisible[index] = !apiStatusMap.isHiddenBoxVisible[index];
  apiStatusMap.isRotated[index] = !apiStatusMap.isRotated[index];
};

// 4. 请求处理
const onSubmit = async (index: number): Promise<void> => {
  apiStatusMap.isLoading[index] = true;
  const {pathParams} = inputValuesMap[index];
  const apiInfo = apiInfoList[index];

  try {
    // 查找对应的 API 方法
    const apiMethod = Object.values(DynamicLogicSwitchListAPI)[index];

    // 发送请求
    const numericParams = pathParams.map(toInteger);
    const response = await apiMethod.request(...numericParams);
    apiInfo.result = JSON.stringify(response, null, 2);
  } catch (error: any) {
    // 错误处理
    apiInfo.result = JSON.stringify({
      error: error.message || '未知错误',
      ...(error.response?.data || {})
    }, null, 2);
    ElMessage.error(error.message || "请求失败，请稍后再试");
  } finally {
    apiStatusMap.isLoading[index] = false;
  }
};


//初始化输入框
const traverseApiInfoList = () => {
  apiInfoList.forEach((apiInfo, index) => {
    initializeInputValues(apiInfo, index);
  });
}
// 5. 生命周期
onMounted(() => {
  traverseApiInfoList();
});
</script>


<style lang="scss" scoped>
.content {
  padding-top: 10px;
  padding-bottom: 12px;
  width: 80%;
  // max-width: 800px;
  height: auto;
  margin: auto auto 20px;
  border-radius: 8px;
}

.info-api {
  height: 5vh;
  margin-top: 0;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 20px;
  border: 1px solid #acaaaa;
  border-radius: 2px;
  background-color: #ffffff;
  cursor: pointer;
}

.transparent-icon {
  width: 20px;
  height: 20px;
  background-color: transparent;
  margin: 0 10px;
  padding: 0;
  transition: transform 0.3s ease;
  cursor: pointer;
}

.info-method {
  width: 10%;
  font-size: 18px;
  font-weight: bold;
  color: green;
  border-right: #cdcdcd solid 1px;
}

.info-url {
  width: 80%;
  font-size: 16px;
}

.el-button {
  background-color: #ffffff;
  color: #409eff;
  border-color: #409eff;

  &:hover {
    background-color: #409eff;
    color: white;
  }

  &:active {
    background-color: #3399ff;
    color: white;
  }
}

.hidden-box {
  width: 100%;
  background-color: #ffffff;
  border: 1px solid #ddd;
  margin-top: 10px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  border-radius: 2px;
}

.inner-card {
  padding: 10px;
}

ul {
  list-style-type: none;
  padding: 0;

  li {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 15px;

    .input-box {
      width: 85%;
      display: flex;
      justify-content: flex-start;
      align-items: center;
      // border: 1px solid red;
      .el-input {
        width: auto;
        max-width: 30%;
        padding-right: 10px;
      }
    }

    span {
      // width: 10%;
      font-size: 16px;
      color: #333;
    }

    i .el-icon .el-tooltip__trigger {
      margin-left: -0px;
    }

    .el-input {
      width: 85%;
    }
  }
}

.result-info {
  width: 85%;
  padding: 15px;
  background-color: #f5f5f5;
  border-radius: 8px;
  border: 1px solid #ccc;
  min-height: 100px;
  font-size: 14px;
  color: #333;
  word-wrap: break-word;
  white-space: pre-wrap;
}

.info-api:hover .transparent-icon {
  transform: rotate(180deg);
}

/* 移动端适配 */
@media (max-width: 768px) {
  .content {
    width: 100%;
  }

  .info-api {
    height: auto;
  }

  .info-url {
    font-size: 12px;
  }
  .hidden-box ul li {
    flex-direction: column;
  }

  .result-info {
    font-size: 12px;
  }
}
</style>