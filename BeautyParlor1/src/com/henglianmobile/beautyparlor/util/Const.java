package com.henglianmobile.beautyparlor.util;
/**
 * ������������ӿ�
 * @author Administrator
 *
 */
public class Const {
	
	/**ip*/
	public static final String BASEIP = "http://115.28.147.21:1819";
	
	public static final int PAGEROWS = 10;
	/**ע��*/
	public static final String REGISTERURL = BASEIP + "/api/users/register?";
	/** ����Ժ�ʸ���֤�ӿ� */
	public static final String BEAUTYPARLORQUALIFICATIONURL = BASEIP + "/includeImg/upShopInfo.ashx?";
	/**��֤�ֻ����Ƿ��Ѿ�ע��*/
	public static final String PHONEISEXISTURL = BASEIP + "";
	/**��¼*/
	public static final String LOGONURL = BASEIP + "/api/users/login?";
	/**��ȡ��֤��ӿ�*/
	public static final String GETCODEURL = BASEIP + "/api/users/sendmsg?mobile=";
	/**�ϴ�ͼƬ�ӿ�*/
	public static final String UPLOADPICTUREURL = BASEIP + "/bllCommon/upimg.ashx";
	/**��ȡ������Ϣ�ӿ�*/
	public static final String GETUSERINFODETAILURL = BASEIP + "/api/users/getInfo?uid=";
	/**�޸ĸ�����Ϣ�ӿ�*/
	public static final String UPDATEUSERINFODETAILURL = BASEIP + "/api/users/upMyInfo?";
	/**�޸�����ӿ�*/
	public static final String UPDATEPASSWORDURL = BASEIP + "";
	/**�޸��ֻ��Žӿ�*/
	public static final String UPDATEMOBILEURL = BASEIP + "";
	/**��������ӿ�*/
	public static final String FORGETPASSWORDURL = BASEIP + "/api/users/findPsw?";
	/**�ϴ�ͷ��ӿ�*/
	public static final String UPLOADPHOTOURL = BASEIP + "/bllCommon/upheadimg.ashx?";
	
	/**����Ȧ�ӿ�*/
	/**��ȡ����Ȧ�б�ӿ�*/
	public static final String GETMEIYOUQUANLISTURL = BASEIP + "/api/topic/topicADList?";
	/**��ȡ����Ȧ��������ӿ�*/
	public static final String GETMEIYOUQUANDETAILLISTURL = BASEIP + "/api/topic/getInfo?tId=";
	/**��ȡ����Ȧ�������۽ӿ�*/
	public static final String GETMEIYOUQUANDETAILCOMMENTURL = BASEIP + "/api/comment/commentlist?tid=";
	/**��������Ȧ�������۽ӿ�*/
	public static final String ADDMEIYOUQUANCOMMENTURL = BASEIP + "/api/comment/addcomment?";
	/**��������Ȧ���ӽӿ�*/
	public static final String PUBLISHMEIYOUQUANURL = BASEIP + "/api/topic/addtopic?";
	/**����Ȧ�ӿ�*/
	
	/**��Ѷ�ӿ�*/
	/**��ȡ��Ѷ�б�ӿ�*/
	public static final String GETZIXUNLISTURL = BASEIP + "/api/new/getnew?";
	/**��ȡ��Ѷ����ӿ�*/
	public static final String GETZIXUNDETAILURL = BASEIP + "/newsView.aspx?";
	
	/**��Ѷ�ӿ�*/
	
	/**�����߽ӿ�*/
	/**�����߷������󷽰��ӿ�*/
	public static final String CONSUMERPUBLISHPROPOSALREQUESTURL = BASEIP + "/api/program/addprogram?";
	/**�����߷������󷽰���ȡ����ӿ�*/
	public static final String CONSUMERGETPROJECTTYPEURL = BASEIP + "/api/program/beautyType";
	/**�����߻�ȡ�ҵķ����б�ӿ�*/
	public static final String GETMYPROPOSALLISTURL = BASEIP + "/api/program/toMyProg?";
	/**��ȡ����Ȧ���������Ƴ��ķ����ظ��б�ӿ�*/
	public static final String GETFANGANDETAILCOMMENTURL = BASEIP + "";
	/**��������Ȧ���������Ƴ��ķ����ظ��ӿ�*/
	public static final String ADDFANGANCOMMENTURL = BASEIP + "";
	/**�����ѽӿ�*/
	public static final String SEARCHFRIENDSURL = BASEIP + "/api/users/getlist?";
	/**��Ӻ��ѽӿ�*/
	public static final String ADDFRIENDSURL = BASEIP + "/api/friend/addfriend?";
	/** ɾ�����ѽӿ�*/
	public static final String DELETEFRIENDSURL = BASEIP + "/api/friend/delfriend?fid=";
	/**��ȡ�����б�ӿ�*/
	public static final String GETFRIENDSURL = BASEIP + "/api/friend/getfriend?";
	/**��ȡ��Ϣ�б�ӿ�*/
	public static final String GETINFORMATIONURL2 = BASEIP + "/api/message/messList?";
	/**��������Ժ�Ƴ��ķ���������Ȧ�ӿ�*/
	public static final String SHAREPROPOSALTOMEIYOUQUANURL = BASEIP + "/api/program/enableShared?";
	
	/**����Ժ�ӿ�*/
	/**����Ժ��ȡ�ҵĹ���б�ӿ�*/
	public static final String GETMYGUANGGAOLISTURL = BASEIP + "/api/ad/getad?";
	/**����Ժ��ȡ�ҵĹ������ӿ�*/
	public static final String GETMYGUANGGAODETAILURL = BASEIP + "/api/ad/getadinfo?adid=";
	/**����Ժ�񷢲����ӿ�*/
	public static final String PUBLISHGUANGGAO = BASEIP + "/api/ad/intoad?";
	/**����Ժ��ȡ��Ϣ�б�ӿ�*/
	public static final String GETINFOMATIONSURL = BASEIP + "/api/message/messageList?";
	/**����Ժ��ȡ�ҵķ����б�ӿ�*/
	public static final String GETPROPOSALLISTDETAIL = BASEIP + "/api/program/getmyprogram?";
	/**����Ժ��ȡ�ҵ�������ϸ�ӿ�*/
	public static final String GETEXPENSEDETAILURL = BASEIP + "/api/salesShop/shopConsume?";
	/**����Ժ��ȡ�˻����ӿ�*/
	public static final String GETBALANCEURL = BASEIP + "/api/salesShop/getCurrentBalance?userId=";
	/**����Ժ�������֧���ӿ�*/
	public static final String PAYFROMBALANCEURL = BASEIP + "/api/salesShop/addChange?";
	
	/**��Ϣ*/
	/** �Ƿ�ͬ������������ӿ� */
	public static final String DEALFRIENDSREQUESTURL = BASEIP + "/api/friend/dealfriend?";
	/**��Ϣ*/
	
	/** ҵ��Ա�ӿ� */
	/** �ҵ�����Ժ�б����ɽӿ� */
	public static final String GETMYBEAUTYPARLORURL = BASEIP + "/api/salesShop/shopList?";
	
	/**��ȡ��������ӿ�*/
	public static final String GETPROPOSALREQUESTDETAIL = BASEIP + "/api/program/getproinfo?pid=";
	/**���޽ӿ�*/
	public static final String CLICKZANURL = BASEIP + "/api/topic/toLike?";
	/**��ȡ�۸�ӿ�*/
	public static final String GETPRICEURL = BASEIP + "/api/program/getPrice?aaa=";

	/** ��ȡapk�汾XML�ӿ� */
	public static final String APK_VERSION_URL = "http://115.28.147.21:1819/apk/apkversion.xml";
}
