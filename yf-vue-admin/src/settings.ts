import {SizeEnum} from "./enums/SizeEnum";
import {LayoutEnum} from "./enums/LayoutEnum";
import {ThemeEnum} from "./enums/ThemeEnum";
import {AppSettings, AppState} from "@/store/modules/system";
import {DeviceEnum} from "@/enums/DeviceEnum";
import {SidebarStatusEnum} from "@/enums/SidebarStatusEnum";

const {pkg} = __APP_INFO__;

interface DefaultSettings {
    title: string,
    version: string,
    watermarkContent: string,
    app: AppState,
    settings: AppSettings
}

const defaultSettings: DefaultSettings = {
    title: pkg.name,
    version: pkg.version,
    watermarkContent: pkg.name,
    app: {
        size: SizeEnum.DEFAULT,
        device: DeviceEnum.DESKTOP,
        sidebarStatus: SidebarStatusEnum.CLOSED,
        tourStatus: true
    },
    settings: {
        settingsVisible: false,
        fixedHeader: true,
        tagsView: true,
        sidebarLogo: true,
        breadCrumb: true,
        layout: LayoutEnum.LEFT,
        theme: ThemeEnum.LIGHT,
        themeColor: {
            info: "#909399",
            primary: "#409EFF",
            success: "#67C23A",
            warning: "#E6A23C",
            danger: "#F56C6C"
        },
        animateCss: 'animate__fadeIn',
        watermarkEnabled: true
    }
};

export default defaultSettings;
