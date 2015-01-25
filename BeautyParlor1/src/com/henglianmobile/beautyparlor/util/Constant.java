package com.henglianmobile.beautyparlor.util;


/**
 * 此类用来存放各种常量
 * @author Administrator
 *
 */
public class Constant {
	
	public static final boolean ISRELEASE = false;
	/**注册类型1-美容院*/
	public static final String BEAUTYPARLOR = "1";
	/**注册类型3-消费者*/
	public static final String CONSUMER = "3";
	/**注册类型4-业务员*/
	public static final String SALEMAN = "4";
	
	public static final String[] ITEMS = new String[] { "相册", "拍照" };
	public static final int SELECTCARBRAND = 0;
	public static final int REQUEST_CAMERA = 1;
	public static final int REQUEST_CHOOSE = 2;
	
	/**选择照片标记*/
	public static final String PHOTOTAG = "photo";
	/**选择照片标记--1 发布美友圈选择照片*/
	public static final String PIC_MEIYOUQUAN = "1";
	/**选择照片标记--2 消费者发布方案请求选择照片*/
	public static final String PIC_PROPOSALREQUEST = "2";
	/**选择照片标记--3 美容院发布广告选择照片*/
	public static final String PIC_PUBLISH_GUANGGAO = "3";
	
	/**上传图片类型*/
	/**广告*/
	public static final String GUANGAO = "1";
	/**方案*/
	public static final String FANGAN = "2";
	/**咨询*/
	public static final String ZIXUN = "3";
	/**美友圈*/
	public static final String MEIYOUQUAN = "4";
	
	/**发送广播*/
	/**发布美友圈图片接收广播*/
	public static final String ACTIONMEIYOUQUANSELECTPHOTO = "com.henglianmobile.beautyparlor.ui.activity.PublishMeiYouQuanActivity";
	/**消费者发布方案请求图片接收广播*/
	public static final String ACTIONCONSUMERPROPOSALREQUESTSELECTPHOTO = "com.henglianmobile.beautyparlor.ui.activity.consumer.ProposalRequestPublishActivity";
	/**美容院发布广告图片接收广播*/
	public static final String ACTIONBEAUTYPARLORPULISHGUANGGAOSELECTPHOTO = "com.henglianmobile.beautyparlor.ui.activity.beautyparlor.GuangGaoPublishActivity";
	
	
	/**从首页进入子菜单返回的广播*/
	public static final String SHOUYETOOTHERACTIVITY = "com.henglianmobile.beautyparlor.activity.MainActivity";
	/**美容院从首页进入我的方案广播*/
	public static final String SHOUYEBEAUTYPARLORMYPROPOSAL = "com.henglianmobile.beautyparlor.ui.fragment.MeBeautyParlorFragment";
	/**美容院从首页进入我的方案方案列表广播*/
	public static final String SHOUYEBEAUTYPARLORMYPROPOSALUPDATE = "com.henglianmobile.beautyparlor.ui.fragment.MeBeautyParlorFragment111";
	/**修改个人资料发送广播*/
	public static final String UPDATEBASEINFOACTION = "com.henglianmobile.beautyparlor.ui.updatebaseinformation";
	
	
	/**推出方案支付成功发出的广播*/
	public static final String ACTIONPUSHPROPOSALSUCCESS = "com.henglianmobile.beautyparlor.ui.activity.beautyparlor.PushProposalPublishActivity.pay";
	/**发布广告支付成功发出的广播*/
	public static final String ACTIONPUSHGUANGGAOSUCCESS = "com.henglianmobile.beautyparlor.ui.activity.beautyparlor.GuangGaoPublishActivity.pay";

	
	/** 消息类型 */
	/** 方案 */
	public static final String INFO_FANGAN = "fa";
	/** 好友 */
	public static final String INFO_FRIENDS = "hy";
	/** 系统消息 */
	public static final String INFO_SYSTEM = "zx";
	
	
	/** 帖子类型 */
	/** 原生帖子--4  */
	public static final int TIE_YUANSHENG = 4;
	/** 有广告发过来的帖子--1 */
	public static final int TIE_GUANGGAO = 1;
	/** 由用户分享方案过来的帖子--2 */
	public static final int TIE_FANGAN = 2;
	
	/**业务员-我的美容院，我的提成-类别*/
	/**业务员-我的美容院 */
	public static final int BEAUTYPARLORTAG = 0;
	/**业务员-我提成 */
	public static final int COMMISSIONTAG = 1;
	
	
	/**美容院-支付类型*/
	/**美容院发布广告支付 */
	public static final int PUBLISHGUANGGAO = 1;
	/**美容院推出方案支付 */
	public static final int PUSHPROPOSAL = 2;
}
