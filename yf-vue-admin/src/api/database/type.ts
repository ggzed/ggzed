/**
 * 总览数据
 */
interface OverviewData {
    cardInfo: Array<CardInfo>;
}

/**
 * 上方卡片
 */
interface CardInfo {
    name: string;
    number: number;
    size: string;
    status: number;
}

