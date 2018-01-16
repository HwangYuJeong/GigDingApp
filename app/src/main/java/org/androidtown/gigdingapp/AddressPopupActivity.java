package org.androidtown.gigdingapp;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.TextView;

/**
 * Created by Kim on 2018-01-15.
 * 출처: http://codeman77.tistory.com/55 [☆]
 */

public class AddressPopupActivity extends Activity{

    private WebView findAddress;
    private TextView resultAddress;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.address_popup);


        resultAddress = (TextView) findViewById(R.id.Address_result);

        // WebView 초기화
        init_webView();

        // 핸들러를 통한 JavaScript 이벤트 반응
        handler = new Handler();
    }

    public void init_webView() {
        // WebView 설정
        findAddress = (WebView) findViewById(R.id.Address_wv);
        // JavaScript 허용
        findAddress.getSettings().setJavaScriptEnabled(true);
        // JavaScript의 window.open 허용
        findAddress.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        // JavaScript이벤트에 대응할 함수를 정의 한 클래스를 붙여줌
        // 두 번째 파라미터는 사용될 php에도 동일하게 사용해야함
        findAddress.addJavascriptInterface(new AndroidBridge(), "TestApp");
        // web client 를 chrome 으로 설정
        findAddress.setWebChromeClient(new WebChromeClient());
        // webview url load
        findAddress.loadUrl("http://www.calebslab.com/calebslab/caleb/android_post.php");

    }

    private class AndroidBridge {
        @JavascriptInterface
        public void setAddress(final String arg1, final String arg2, final String arg3) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    resultAddress.setText(String.format("(%s) %s %s", arg1, arg2, arg3));
                    // WebView를 초기화 하지않으면 재사용할 수 없음
                    init_webView();
                }
            });
        }
    }
}
