const baseUrl = "http://yf.wiki/images/avatar-frame/"; // 节日头像地址

export interface AvatarFrame {
    id: number;              // 头像框的唯一ID
    tag: string;             // 节日的标志，如 "national"、"spring"
    name: string;            // 节日的名称，如 "国庆节"
    description: string;     // 对应节日的中文描述
    totalImages: number;     // 图片总数量
    imageUrls: string[];     // 子图片链接数组
}


/**
 * 节日类别
 */
function getAvatarFrameCategory(): AvatarFrame[] {
    return [
        {
            id: 1,
            tag: "national",
            name: "国庆节",
            description: "国庆节是庆祝中华人民共和国成立的重要节日，举国上下欢庆，充满爱国情怀与喜悦。",
            totalImages: 15,
            imageUrls: []
        },
        {
            id: 2,
            tag: "spring",
            name: "春节",
            description: "春节是中国最重要的传统节日，象征着新年的到来，家人团聚，张灯结彩，燃放烟花以庆祝新春。",
            totalImages: 2,
            imageUrls: []
        },
        {
            id: 3,
            tag: "mid-autumn",
            name: "中秋节",
            description: "中秋节象征着团圆和丰收，赏月、吃月饼是这一节日的传统活动，充满温馨与亲情。",
            totalImages: 7,
            imageUrls: []
        },
        {
            id: 4,
            tag: "valentine",
            name: "情人节",
            description: "情人节是情侣们表达爱意的重要日子，通常伴随着送花、巧克力和浪漫约会。",
            totalImages: 0,
            imageUrls: []
        },
        {
            id: 5,
            tag: "labour",
            name: "劳动节",
            description: "劳动节是全球劳动者共同庆祝的节日，象征着劳动光荣与社会进步。",
            totalImages: 0,
            imageUrls: []
        },
        {
            id: 6,
            tag: "children",
            name: "儿童节",
            description: "儿童节是为孩子们设立的特别节日，充满了欢笑、游戏和各种娱乐活动。",
            totalImages: 0,
            imageUrls: []
        },
        {
            id: 7,
            tag: "christmas",
            name: "圣诞节",
            description: "圣诞节是西方最重要的节日之一，象征着家庭团聚、爱与希望。人们通过装饰圣诞树、交换礼物来庆祝。",
            totalImages: 4,
            imageUrls: []
        },
        {
            id: 8,
            tag: "halloween",
            name: "万圣节",
            description: "万圣节是充满惊悚和趣味的节日，孩子们穿上奇装异服，挨家挨户讨糖。",
            totalImages: 1,
            imageUrls: []
        },
        {
            id: 9,
            tag: "dragon-boat",
            name: "端午节",
            description: "端午节是纪念屈原的传统节日，赛龙舟、吃粽子是这一节日的主要活动。",
            totalImages: 0,
            imageUrls: []
        },
        {
            id: 10,
            tag: "tanabata",
            name: "七夕节",
            description: "七夕节是中国的传统情人节，象征着牛郎织女的爱情故事，情侣们在这天互相赠送礼物表达爱意。",
            totalImages: 0,
            imageUrls: []
        },
    ]
}

/**
 * 加载节日头像框链接
 */
function loadAvatarFrameChildren(avatarFrames: AvatarFrame[]): AvatarFrame[] {
    return avatarFrames.map(item => {
        // 动态拼接字图片链接
        for (let i = 1; i <= item.totalImages; i++) {
            item.imageUrls.push(`${baseUrl}${item.tag}/${item.tag}-avatar-${i}.png`);
        }
        // 返回头像框信息
        return item;
    })
}

/**
 * 获取头像框
 */
export function getAvatarFrame(): AvatarFrame[] {
    let avatarFrameCategory = getAvatarFrameCategory();
    return loadAvatarFrameChildren(avatarFrameCategory);
}