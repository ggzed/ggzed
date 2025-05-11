<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Preview</title>
    <style>
        /* 优化后的基础样式 */
        body {
            font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, sans-serif;
            margin: 0;
            color: #2c3e50;
            background: #f8fafc;
        }

        .container {
            padding: 24px;
            max-width: 1400px;
            margin: 0 auto;
            background: white;
            border-radius: 12px;
            box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
        }

        /* 搜索区域优化 */
        .search-header {
            display: flex;
            gap: 16px;
            flex-wrap: wrap;
            padding: 16px;
            background: #f8fafc;
            border-radius: 8px;
            margin-bottom: 24px;
        }

        /* 输入框统一样式 */
        .el-input {
            padding: 8px 12px;
            border: 1px solid #e2e8f0;
            border-radius: 6px;
            font-size: 14px;
            transition: all 0.2s ease;
            width: 200px;
        }

        .el-input:focus {
            border-color: #3b82f6;
            box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
            outline: none;
        }

        /* 按钮优化 */
        .el-button {
            padding: 8px 16px;
            border-radius: 6px;
            font-weight: 500;
            transition: all 0.2s ease;
            border: 1px solid transparent;
        }

        .el-button--primary {
            background: #3b82f6;
            color: white;
        }

        .el-button--primary:hover {
            background: #2563eb;
        }

        .el-button--danger {
            background: #ef4444;
            color: white;
        }

        .el-button--danger:hover {
            background: #dc2626;
        }

        /* 表格优化 */
        .el-table {
            border-radius: 8px;
            overflow: hidden;
            box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
        }

        .el-table th {
            background: #f1f5f9;
            font-weight: 600;
            color: #1e293b;
            padding: 6px 8px;
            min-width: 120px; /* 新增最小宽度设置 */
        }

        .el-table td {
            padding: 12px 16px;
            border-color: #f1f5f9;
        }

        .el-table tr:hover td {
            background: #f8fafc;
        }

        /* 分页优化 */
        .pagination {
            margin-top: 24px;
            display: flex;
            align-items: center;
            justify-content: flex-end;
            gap: 12px;
        }

        .pagination span {
            color: #64748b;
        }

        /* 对话框优化 */
        .dialog-mask {
            display: none;
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: rgba(0, 0, 0, 0.4);
            z-index: 1000;

            /* 新增居中方案 */
            align-items: center;
            justify-content: center;
            overflow-y: auto; /* 允许对话框整体滚动 */
        }

        /* 对话框内容区域 */
        .dialog-content {
            padding: 20px 24px;
            overflow-y: auto; /* 内容单独滚动 */
            flex: 1;
        }

        /* 处理超长内容 */
        .dialog-content form {
            min-height: min-content; /* 保持表单最小高度 */
        }

        /* 移动端适配 */
        @media (max-width: 768px) {
            .dialog-wrapper {
                width: 95%;
                max-height: 95vh;
                margin: 20px 0;
            }
        }

        /* 添加滚动条样式 */
        .dialog-content::-webkit-scrollbar {
            width: 6px;
        }

        .dialog-content::-webkit-scrollbar-track {
            background: #f1f5f9;
        }

        .dialog-content::-webkit-scrollbar-thumb {
            background: #cbd5e1;
            border-radius: 3px;
        }

        /* 增加删除对话框特定样式 */
        .delete-confirm {
            text-align: center;
            padding: 40px 20px;
        }

        .delete-confirm p {
            font-size: 16px;
            color: #64748b;
            margin-bottom: 30px;
        }

        .dialog-mask.active .dialog-wrapper {
            transform: scale(1);
            opacity: 1;
        }

        /* 对话框内容容器 */
        .dialog-wrapper {
            background: white;
            width: 90%;
            max-width: 600px;
            max-height: 90vh; /* 限制最大高度 */
            border-radius: 12px;
            box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.1);
            position: relative;
            margin: 10px auto;

            /* 添加滚动 */
            display: flex;
            flex-direction: column;
            transform: scale(0.95);
            opacity: 0;
            transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
        }

        .dialog-header {
            padding: 20px 24px;
            font-size: 18px;
            font-weight: 600;
            color: #1e293b;
            border-bottom: 1px solid #f1f5f9;
            margin: 0 auto;
        }

        /* 表单元素优化 */
        form {
            padding: 20px 24px;
        }

        form > div {
            margin-bottom: 16px;
            display: flex;
            align-items: center;
            gap: 12px;
        }

        label {
            width: 120px;
            text-align: right;
            color: #475569;
            font-size: 14px;
        }

        textarea.el-input {
            height: 100px;
            padding: 12px;
            line-height: 1.5;
        }

        /* 删除确认对话框优化 */
        .delete-confirm {
            text-align: center;
            padding: 24px;
        }

        .delete-confirm p {
            color: #64748b;
            margin-bottom: 24px;
        }

        /* 移动端适配 */
        @media (max-width: 768px) {
            .container {
                padding: 16px;
                border-radius: 0;
            }

            .search-header {
                flex-direction: column;
            }

            .el-input {
                width: 100%;
            }

            form > div {
                flex-direction: column;
                align-items: flex-start;
            }

            label {
                text-align: left;
                width: auto;
            }
        }
    </style>
</head>
<body>
<div class="container">
    <!-- 搜索区域 -->
    <div class="search-header">
        <#list mapFields.query as field>
            <#if field.queryType != "between">
                <#if field.queryFormType == "input">
                    <input class="el-input" type="text" placeholder="${field.showName}" name="${field.javaTsFieldName}">
                <#elseif field.queryFormType == "select">
                    <select class="el-input" name="${field.javaTsFieldName}">
                        <option value="demo_one">${field.javaTsFieldName}内容1</option>
                        <option value="demo_two">${field.javaTsFieldName}内容2</option>
                    </select>
                <#elseif field.queryFormType == "multi_select" && field.dictTypeName?has_content>
                    <select class="el-input" name="${field.javaTsFieldName}">
                        <option value="demo_one">${field.javaTsFieldName}内容1</option>
                        <option value="demo_two">${field.javaTsFieldName}内容2</option>
                    </select>
                <#elseif field.queryFormType == "datetime">
                    <input class="el-input" type="datetime-local" placeholder="${field.showName}"
                           name="${field.javaTsFieldName}">
                <#elseif field.queryFormType == "date">
                    <input class="el-input" type="date" placeholder="${field.showName}" name="${field.javaTsFieldName}">
                <#else >
                    <input class="el-input" type="text" placeholder="${field.showName}" name="${field.javaTsFieldName}">
                </#if>
            <#elseif field.queryType == "between" && !(field.dictTypeName?has_content)>
            <#elseif field.queryFormType == "datetime">
                <input class="el-input" type="datetime-local" placeholder="${field.showName}开始"
                       name="${field.javaTsFieldName}">
                <input class="el-input" type="datetime-local" placeholder="${field.showName}结束"
                       name="${field.javaTsFieldName}">
            <#elseif field.queryFormType == "date">
                <input class="el-input" type="date" placeholder="${field.showName}开始" name="${field.javaTsFieldName}">
                <input class="el-input" type="date" placeholder="${field.showName}结束" name="${field.javaTsFieldName}">
            <#else >
                <input class="el-input" type="text" placeholder="${field.showName}开始" name="${field.javaTsFieldName}">
                <input class="el-input" type="text" placeholder="${field.showName}结束" name="${field.javaTsFieldName}">
            </#if>
        </#list>
        <button class="el-button el-button--primary">搜索</button>
        <button class="el-button el-button--primary" onclick="showDialog('add')">新增</button>
    </div>

    <!-- 表格容器 -->
    <div class="table-container">
        <table class="el-table">
            <thead>
            <tr>
                <#list mapFields.show as field>
                    <th>${field.showName}</th>
                </#list>
                <th style="width: 150px">操作</th>
            </tr>
            </thead>
            <tbody id="tableBody">
            <tr>
                <#list mapFields.show as field>
                    <#if field.showType == "image">
                        <td><img src="#" style="max-width: 120px"/></td>
                    <#elseif field.showType == "href">
                        <td><a href="#">示例链接</a></td>
                    <#else>
                        <td>${field.javaTsFieldName}</td>
                    </#if>
                </#list>
                <td>
                    <button class="el-button" onclick="showDialog('edit')">编辑</button>
                    <button class="el-button el-button--danger" onclick="showDeleteConfirm()">删除</button>
                </td>
            </tr>
            </tbody>
        </table>
    </div>

    <!-- 分页 -->
    <div class="pagination">
        <button class="el-button">上一页</button>
        <span>当前页：1</span>
        <button class="el-button">下一页</button>
    </div>

    <!-- 新增/编辑对话框 -->
    <div class="dialog-mask" id="dialogMask">
        <div class="dialog-wrapper">
            <div class="dialog-header" id="dialogTitle">新增数据</div>
            <div class="dialog-content">
                <form id="dataForm" onsubmit="handleSubmit(event)">
                    <#list mapFields.form as field>
                        <#if field.saveFormType == "image" || field.saveFormType == "file">
                            <div style="margin: 15px 0;">
                                <label>${field.showName}：</label>
                                <input class="el-input" type="file" placeholder="请选择${field.showName}"
                                       name="${field.javaTsFieldName}">
                            </div>
                        <#elseif field.saveFormType == "text_area">
                            <div style="margin: 15px 0;">
                                <label>${field.showName}：</label>
                                <textarea class="el-input" placeholder="请输入${field.showName}"
                                          name="${field.javaTsFieldName}"></textarea>
                            </div>
                        <#elseif field.saveFormType == "date">
                            <div style="margin: 15px 0;">
                                <label>${field.showName}：</label>
                                <input class="el-input" type="date" placeholder="请选择${field.showName}"
                                       name="${field.javaTsFieldName}">
                            </div>
                        <#elseif field.saveFormType == "datetime">
                            <div style="margin: 15px 0;">
                                <label>${field.showName}：</label>
                                <input class="el-input" type="datetime-local" placeholder="请选择${field.showName}"
                                       name="${field.javaTsFieldName}">
                            </div>
                        <#else>
                            <div style="margin: 15px 0;">
                                <label>${field.showName}：</label>
                                <input class="el-input" type="text" placeholder="请输入${field.showName}"
                                       name="${field.javaTsFieldName}">
                            </div>
                        </#if>
                    </#list>
                    <div style="text-align: right;justify-content: end;margin-right: 20px;">
                        <button type="button" class="el-button" onclick="closeDialog()">取消</button>
                        <button type="submit" class="el-button el-button--primary">确认</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
    <!-- 删除确认对话框 -->
    <!-- 修改删除对话框结构 -->
    <div class="dialog-mask" id="deleteDialog">
        <div class="dialog-wrapper">
            <div class="dialog-header">删除确认</div>
            <div class="dialog-content"> <!-- 新增内容容器 -->
                <div class="delete-confirm">
                    <p>确定要删除这条数据吗？</p>
                    <div style="margin-top: 20px;">
                        <button class="el-button" onclick="closeDeleteDialog()">取消</button>
                        <button class="el-button el-button--danger" onclick="confirmDelete()">确认删除</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script>

    function handleSubmit(event) {
        event.preventDefault();
        closeDialog();
    }

    function showDialog(type) {
        const dialog = document.getElementById('dialogMask');
        dialog.style.display = 'flex'; // 改为 flex 显示
        setTimeout(() => dialog.classList.add('active'), 10);

        // 重置滚动位置
        dialog.querySelector('.dialog-content').scrollTop = 0;
        const title = document.getElementById('dialogTitle');
        title.textContent = type === 'add' ? '新增数据' : '编辑数据';
        dialog.style.display = 'block';
    }

    function closeDialog() {
        document.querySelectorAll('.dialog-mask').forEach(dialog => {
            dialog.classList.remove('active');
            setTimeout(() => dialog.style.display = 'none', 300); // 等待动画完成
        });
    }

    function confirmDelete() {
        closeDialog();
    }

    // 修改删除对话框显示逻辑
    function showDeleteConfirm() {
        const dialog = document.getElementById('deleteDialog');
        dialog.style.display = 'flex';  // 关键修改点1
        setTimeout(() => dialog.classList.add('active'), 10);

        // 重置滚动位置
        dialog.querySelector('.dialog-content').scrollTop = 0;
    }

    // 修改关闭逻辑
    function closeDeleteDialog() {
        const dialog = document.getElementById('deleteDialog');
        dialog.classList.remove('active');
        setTimeout(() => dialog.style.display = 'none', 300);
    }

    // 统一处理背景点击关闭
    document.querySelectorAll('.dialog-mask').forEach(dialog => {
        dialog.addEventListener('click', function (e) {
            if (e.target === this) {
                this.classList.remove('active');
                setTimeout(() => this.style.display = 'none', 300);
            }
        });
    });
</script>
</body>
</html>