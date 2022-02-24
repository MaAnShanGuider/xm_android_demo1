package com.zhangxueyou.android2.activity.task.index;

import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.zhangxueyou.android2.R;
import com.zhangxueyou.android2._commonActivity.CustomTitleBar;
import com.zhangxueyou.android2.databinding.FragmentFirstBinding;

public class FirstFragment extends Fragment {

    private static final String TAG = "requestResult";
    private FragmentFirstBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        View view = binding.getRoot();




        return view;

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        WebView webView = (WebView) view.findViewById(R.id.xm_webView);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        webView.loadUrl("http://192.168.110.37:9010/");
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //使用WebView加载显示url
                view.loadUrl(url);
                //返回true
                return true;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                switch (error.getPrimaryError()) {
                    case SslError.SSL_UNTRUSTED:
                        Log.d(TAG, "onReceivedSslError: The certificate authority is not trusted.");
                        break;
                    case SslError.SSL_EXPIRED:
                        Log.d(TAG, "onReceivedSslError: The certificate has expired.");
                        break;
                    case SslError.SSL_IDMISMATCH:
                        Log.d(TAG, "onReceivedSslError: The certificate Hostname mismatch.");
                        break;
                    case SslError.SSL_NOTYETVALID:
                        Log.d(TAG, "onReceivedSslError: The certificate is not yet valid.");
                        break;
                }
                handler.proceed();
            }
        });
        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                NavHostFragment.findNavController(FirstFragment.this) .navigate(R.id.action_FirstFragment_to_SecondFragment);
                Log.v("LOGV", "结果");
            }
        });


        CustomTitleBar titleBar = view.findViewById(R.id.titlebar);
        titleBar.setLeftIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "左边", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}