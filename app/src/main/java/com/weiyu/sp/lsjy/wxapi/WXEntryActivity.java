package com.weiyu.sp.lsjy.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelbiz.SubscribeMessage;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.modelbiz.WXOpenBusinessView;
import com.tencent.mm.opensdk.modelbiz.WXOpenBusinessWebview;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.ShowMessageFromWX;
import com.tencent.mm.opensdk.modelmsg.WXAppExtendObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.weiyu.sp.lsjy.R;
import com.weiyu.sp.lsjy.base.Constant;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;


public class WXEntryActivity extends Activity implements IWXAPIEventHandler{
	private static String TAG = "MicroMsg.WXEntryActivity";

    private IWXAPI api;
    private MyHandler handler;
	private static String accessToken;

	private static class MyHandler extends Handler {
		private final WeakReference<WXEntryActivity> wxEntryActivityWeakReference;

		public MyHandler(WXEntryActivity wxEntryActivity){
			wxEntryActivityWeakReference = new WeakReference<WXEntryActivity>(wxEntryActivity);
		}

		@Override
		public void handleMessage(Message msg) {
			int tag = msg.what;
			switch (tag) {
				case 1: {
					Bundle data = msg.getData();
					JSONObject json = null;
					try {
						json = new JSONObject(data.getString("result"));
						String openId;
						String refreshToken;
						String scope;
						openId = json.getString("openid");
						accessToken = json.getString("access_token");
						refreshToken = json.getString("refresh_token");
						scope = json.getString("scope");
//						Intent intent = new Intent(wxEntryActivityWeakReference.get(), SendToWXActivity.class);
//						intent.putExtra("openId", openId);
//						intent.putExtra("accessToken", accessToken);
//						intent.putExtra("refreshToken", refreshToken);
//						intent.putExtra("scope", scope);
//						wxEntryActivityWeakReference.get().startActivity(intent);
					} catch (JSONException e) {
						Log.e(TAG, e.getMessage());
					}
				}
			}
		}
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		api = WXAPIFactory.createWXAPI(this,null);
//    	api = WXAPIFactory.createWXAPI(this, Constant.APP_ID, false);
		handler = new MyHandler(this);

        try {
            Intent intent = getIntent();
        	api.handleIntent(intent, this);
        } catch (Exception e) {
        	e.printStackTrace();
        }
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
		switch (req.getType()) {
		case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
//			goToGetMsg();
			break;
		case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
//			goToShowMsg((ShowMessageFromWX.Req) req);
			break;
		default:
			break;
		}
        finish();
	}

	@Override
	public void onResp(BaseResp resp) {
		int result = 0;
		
//		switch (resp.errCode) {
//		case BaseResp.ErrCode.ERR_OK:
//			result = R.string.errcode_success;
//			break;
//		case BaseResp.ErrCode.ERR_USER_CANCEL:
//			result = R.string.errcode_cancel;
//			break;
//		case BaseResp.ErrCode.ERR_AUTH_DENIED:
//			result = R.string.errcode_deny;
//			break;
//		case BaseResp.ErrCode.ERR_UNSUPPORT:
//			result = R.string.errcode_unsupported;
//			break;
//		default:
//			result = R.string.errcode_unknown;
//			break;
//		}
		
//		Toast.makeText(this, getString(result) + ", type=" + resp.getType(), Toast.LENGTH_SHORT).show();


		if (resp.getType() == ConstantsAPI.COMMAND_SUBSCRIBE_MESSAGE) {
			SubscribeMessage.Resp subscribeMsgResp = (SubscribeMessage.Resp) resp;
			String text = String.format("openid=%s\ntemplate_id=%s\nscene=%d\naction=%s\nreserved=%s",
					subscribeMsgResp.openId, subscribeMsgResp.templateID, subscribeMsgResp.scene, subscribeMsgResp.action, subscribeMsgResp.reserved);

			Toast.makeText(this, text, Toast.LENGTH_LONG).show();
		}

        if (resp.getType() == ConstantsAPI.COMMAND_LAUNCH_WX_MINIPROGRAM) {
            WXLaunchMiniProgram.Resp launchMiniProgramResp = (WXLaunchMiniProgram.Resp) resp;
            String text = String.format("openid=%s\nextMsg=%s\nerrStr=%s",
                    launchMiniProgramResp.openId, launchMiniProgramResp.extMsg,launchMiniProgramResp.errStr);

            Toast.makeText(this, text, Toast.LENGTH_LONG).show();
        }

        if (resp.getType() == ConstantsAPI.COMMAND_OPEN_BUSINESS_VIEW) {
            WXOpenBusinessView.Resp launchMiniProgramResp = (WXOpenBusinessView.Resp) resp;
            String text = String.format("openid=%s\nextMsg=%s\nerrStr=%s\nbusinessType=%s",
                    launchMiniProgramResp.openId, launchMiniProgramResp.extMsg,launchMiniProgramResp.errStr,launchMiniProgramResp.businessType);

            Toast.makeText(this, text, Toast.LENGTH_LONG).show();
        }

        if (resp.getType() == ConstantsAPI.COMMAND_OPEN_BUSINESS_WEBVIEW) {
            WXOpenBusinessWebview.Resp response = (WXOpenBusinessWebview.Resp) resp;
            String text = String.format("businessType=%d\nresultInfo=%s\nret=%d",response.businessType,response.resultInfo,response.errCode);

            Toast.makeText(this, text, Toast.LENGTH_LONG).show();
        }

		if (resp.getType() == ConstantsAPI.COMMAND_SENDAUTH) {
			SendAuth.Resp authResp = (SendAuth.Resp)resp;
			final String code = authResp.code;
//			EventBus.getDefault().post(new MessageEvent(EventConstants.LOGIN, EventConstants.WX_LOGIN_BACK,code));
//			LoginManager loginManager=new LoginManager(this);
//			Map<String,String>map=new HashMap<>();
//			map.put("code",code);
//			map.put("appFlag","Android");
//			loginManager.loginByWX(map, new Callback() {
//				@Override
//				public void onSuccess(HttpInfo info) throws IOException {
//					TypeToken type= new TypeToken<BaseObjectBean<LoginBean>>(){};
//					BaseObjectBean<LoginBean> loginBean= JsonObjectUtils.parseJsonToBean(info.getRetDetail(),type.getType());
//					if (loginBean.getCode()==200) {
//						LoginBean localBean = loginBean.getData();
//						if (localBean != null) {
//							User.putAppUserName(localBean.getName());
//							User.putAppUserAvatar(localBean.getAvatar());
//							User.putAppUserId(localBean.getId());
//							User.putAppToken(localBean.getToken());
//
//							ActivityController.closeActivity(WebActivity.class);
////							finish();
//						}
//					}else {
//						ToastUtils.show(loginBean.getMsg());
//					}
//				}
//
//				@Override
//				public void onFailure(HttpInfo info) throws IOException {
//
//				}
//			});

//			String url=String.format("https://api.weixin.qq.com/sns/oauth2/access_token?" +
//							"appid=%s&secret=%s&code=%s&grant_type=authorization_code", Constants.APP_ID,
//					Constants.APP_SECRET, code);
//			OkHttpClient okHttpClient = new OkHttpClient();
//			final Request request = new Request.Builder()
//					.url(url)
//					.get()//默认就是GET请求，可以不写
//					.build();
//			Call call = okHttpClient.newCall(request);
//			call.enqueue(new Callback() {
//				@Override
//				public void onFailure(Call call, IOException e) {
//					Log.d(TAG, "onFailure: ");
//				}
//
//				@Override
//				public void onResponse(Call call, Response response) throws IOException {
////					Log.d(TAG, "onResponse: " + response.body().string());
//					String result=response.body().string();
//					try {
//						JSONObject json = new JSONObject(result);
//						String openid = json.getString("openid");
//						String unionid = json.getString("unionid");
//						String access_token = json.getString("access_token");
//						SharedPreferences sharedPreferences=getSharedPreferences(Constant.NATIVE_LOCAL,MODE_PRIVATE);
//						SharedPreferences.Editor editor=sharedPreferences.edit();
//						editor.putString(Constant.OPEN_ID,openid);
//						editor.putString(Constant.ACCESSTOKEN, access_token);
//						editor.putString(Constant.UNION_ID, unionid);
//						editor.commit();
//						EventBus.getDefault().post(new MessageEvent(EventConstants.LOGIN, EventConstants.WX_LOGIN_BACK));
//					} catch (JSONException e) {
//						e.printStackTrace();
//					}
//
//				}
//			});
		}
        finish();
	}
	
	private void goToGetMsg() {
//		Intent intent = new Intent(this, GetFromWXActivity.class);
//		intent.putExtras(getIntent());
//		startActivity(intent);
//		finish();
	}
	
	private void goToShowMsg(ShowMessageFromWX.Req showReq) {
		WXMediaMessage wxMsg = showReq.message;		
		WXAppExtendObject obj = (WXAppExtendObject) wxMsg.mediaObject;
		
		StringBuffer msg = new StringBuffer();
		msg.append("description: ");
		msg.append(wxMsg.description);
		msg.append("\n");
		msg.append("extInfo: ");
		msg.append(obj.extInfo);
		msg.append("\n");
		msg.append("filePath: ");
		msg.append(obj.filePath);
		
//		Intent intent = new Intent(this, ShowFromWXActivity.class);
//		intent.putExtra(Constants.ShowMsgActivity.STitle, wxMsg.title);
//		intent.putExtra(Constants.ShowMsgActivity.SMessage, msg.toString());
//		intent.putExtra(Constants.ShowMsgActivity.BAThumbData, wxMsg.thumbData);
//		startActivity(intent);
//		finish();
	}
}