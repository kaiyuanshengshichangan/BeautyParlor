package com.henglianmobile.beautyparlor.util;
/**
 * 此类用来定义接口
 * @author Administrator
 *
 */
public class Const {
	
	/**ip*/
	public static final String BASEIP = "http://115.28.147.21:1819";
	
	public static final int PAGEROWS = 10;
	/**注册*/
	public static final String REGISTERURL = BASEIP + "/api/users/register?";
	/** 美容院资格认证接口 */
	public static final String BEAUTYPARLORQUALIFICATIONURL = BASEIP + "/includeImg/upShopInfo.ashx?";
	/**验证手机号是否已经注册*/
	public static final String PHONEISEXISTURL = BASEIP + "";
	/**登录*/
	public static final String LOGONURL = BASEIP + "/api/users/login?";
	/**获取验证码接口*/
	public static final String GETCODEURL = BASEIP + "/api/users/sendmsg?mobile=";
	/**上传图片接口*/
	public static final String UPLOADPICTUREURL = BASEIP + "/bllCommon/upimg.ashx";
	/**获取个人信息接口*/
	public static final String GETUSERINFODETAILURL = BASEIP + "/api/users/getInfo?uid=";
	/**修改个人信息接口*/
	public static final String UPDATEUSERINFODETAILURL = BASEIP + "/api/users/upMyInfo?";
	/**修改密码接口*/
	public static final String UPDATEPASSWORDURL = BASEIP + "";
	/**修改手机号接口*/
	public static final String UPDATEMOBILEURL = BASEIP + "";
	/**重置密码接口*/
	public static final String FORGETPASSWORDURL = BASEIP + "/api/users/findPsw?";
	/**上传头像接口*/
	public static final String UPLOADPHOTOURL = BASEIP + "/bllCommon/upheadimg.ashx?";
	
	/**美友圈接口*/
	/**获取美友圈列表接口*/
	public static final String GETMEIYOUQUANLISTURL = BASEIP + "/api/topic/topicADList?";
	/**获取美友圈帖子详情接口*/
	public static final String GETMEIYOUQUANDETAILLISTURL = BASEIP + "/api/topic/getInfo?tId=";
	/**获取美友圈帖子评论接口*/
	public static final String GETMEIYOUQUANDETAILCOMMENTURL = BASEIP + "/api/comment/commentlist?tid=";
	/**发布美友圈帖子评论接口*/
	public static final String ADDMEIYOUQUANCOMMENTURL = BASEIP + "/api/comment/addcomment?";
	/**发布美友圈帖子接口*/
	public static final String PUBLISHMEIYOUQUANURL = BASEIP + "/api/topic/addtopic?";
	/**美友圈接口*/
	
	/**资讯接口*/
	/**获取资讯列表接口*/
	public static final String GETZIXUNLISTURL = BASEIP + "/api/new/getnew?";
	/**获取资讯详情接口*/
	public static final String GETZIXUNDETAILURL = BASEIP + "/newsView.aspx?";
	
	/**资讯接口*/
	
	/**消费者接口*/
	/**消费者发布征求方案接口*/
	public static final String CONSUMERPUBLISHPROPOSALREQUESTURL = BASEIP + "/api/program/addprogram?";
	/**消费者发布征求方案获取分类接口*/
	public static final String CONSUMERGETPROJECTTYPEURL = BASEIP + "/api/program/beautyType";
	/**消费者获取我的方案列表接口*/
	public static final String GETMYPROPOSALLISTURL = BASEIP + "/api/program/toMyProg?";
	/**获取美友圈给消费者推出的方案回复列表接口*/
	public static final String GETFANGANDETAILCOMMENTURL = BASEIP + "";
	/**发送美友圈给消费者推出的方案回复接口*/
	public static final String ADDFANGANCOMMENTURL = BASEIP + "";
	/**搜美友接口*/
	public static final String SEARCHFRIENDSURL = BASEIP + "/api/users/getlist?";
	/**添加好友接口*/
	public static final String ADDFRIENDSURL = BASEIP + "/api/friend/addfriend?";
	/** 删除好友接口*/
	public static final String DELETEFRIENDSURL = BASEIP + "/api/friend/delfriend?fid=";
	/**获取好友列表接口*/
	public static final String GETFRIENDSURL = BASEIP + "/api/friend/getfriend?";
	/**获取消息列表接口*/
	public static final String GETINFORMATIONURL2 = BASEIP + "/api/message/messList?";
	/**分享美容院推出的方案到美友圈接口*/
	public static final String SHAREPROPOSALTOMEIYOUQUANURL = BASEIP + "/api/program/enableShared?";
	
	/**美容院接口*/
	/**美容院获取我的广告列表接口*/
	public static final String GETMYGUANGGAOLISTURL = BASEIP + "/api/ad/getad?";
	/**美容院获取我的广告详情接口*/
	public static final String GETMYGUANGGAODETAILURL = BASEIP + "/api/ad/getadinfo?adid=";
	/**美容院获发布广告接口*/
	public static final String PUBLISHGUANGGAO = BASEIP + "/api/ad/intoad?";
	/**美容院获取消息列表接口*/
	public static final String GETINFOMATIONSURL = BASEIP + "/api/message/messageList?";
	/**美容院获取我的方案列表接口*/
	public static final String GETPROPOSALLISTDETAIL = BASEIP + "/api/program/getmyprogram?";
	/**美容院获取我的消费明细接口*/
	public static final String GETEXPENSEDETAILURL = BASEIP + "/api/salesShop/shopConsume?";
	/**美容院获取账户余额接口*/
	public static final String GETBALANCEURL = BASEIP + "/api/salesShop/getCurrentBalance?userId=";
	/**美容院进行余额支付接口*/
	public static final String PAYFROMBALANCEURL = BASEIP + "/api/salesShop/addChange?";
	
	/**消息*/
	/** 是否同意好友添加请求接口 */
	public static final String DEALFRIENDSREQUESTURL = BASEIP + "/api/friend/dealfriend?";
	/**消息*/
	
	/** 业务员接口 */
	/** 我的美容院列表和提成接口 */
	public static final String GETMYBEAUTYPARLORURL = BASEIP + "/api/salesShop/shopList?";
	
	/**获取方案详情接口*/
	public static final String GETPROPOSALREQUESTDETAIL = BASEIP + "/api/program/getproinfo?pid=";
	/**点赞接口*/
	public static final String CLICKZANURL = BASEIP + "/api/topic/toLike?";
	/**获取价格接口*/
	public static final String GETPRICEURL = BASEIP + "/api/program/getPrice?aaa=";

	/** 获取apk版本XML接口 */
	public static final String APK_VERSION_URL = "http://115.28.147.21:1819/apk/apkversion.xml";
}
