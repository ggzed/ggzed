/**
 * @author 翼飞
 * @date 2025/05/10
 * 解释 : 系统常量
 */
export class SystemConstant {
    /**
     * 系统名称
     */
    public static readonly ADMIN: string = "ADMIN";
}

/**
 * @author 翼飞
 * @date 2024/05/21
 * 解释 : OPTIONS_RADIO 用户增加修改的单选框 , 方便控制顺序
 *       OPTIONS 页面展示的文本 , 注重效率
 *       TAG_STYLE 通过取模决定 展示的样式
 */
export class EnableStatusEnum {
    static readonly OPTIONS_RADIO: OptionType[] = [
        {label: "启用", value: 1},
        {label: "禁用", value: 0}
    ]

    static readonly OPTIONS: Record<string | number, string> = {1: "启用", 0: "禁用"}

    static readonly TAG_STYLE: Array<'success' | 'info' | 'warning' | 'danger' | 'primary'> = ["danger", "success"]
}

export class OperatorLogStatusEnum {
    static readonly OPTIONS_RADIO: OptionType[] = [
        {label: "正常", value: 1},
        {label: "异常", value: 0}
    ]

    static readonly OPTIONS: Record<string | number, string> = {1: "正常", 0: "异常"}

    static readonly TAG_STYLE: Array<'success' | 'info' | 'warning' | 'danger' | 'primary'> = ["danger", "success"]
}

export class DefaultedStatusEnum {
    static readonly OPTIONS_RADIO: OptionType[] = [
        {label: "默认项", value: 1},
        {label: "其他项", value: 0}
    ]

    static readonly OPTIONS: Record<string | number, string> = {1: "默认项", 0: "其他项"}

    static readonly TAG_STYLE: Array<'success' | 'info' | 'warning' | 'danger' | 'primary'> = ["info", "success"]
}

export class HiddenStatusEnum {

    static readonly OPTIONS_RADIO: OptionType[] = [
        {label: "展示", value: 0},
        {label: "隐藏", value: 1}
    ]

    static readonly OPTIONS: Record<string | number, string> = {1: "隐藏", 0: "显示"}

    static readonly TAG_STYLE: Array<'success' | 'info' | 'warning' | 'danger' | 'primary'> = ["success", "danger"]
}
