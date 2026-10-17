package com.lamaa.studio;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.graphics.Color;
import android.graphics.Insets;
import android.os.Build;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowInsets;

import java.io.OutputStream;
import java.io.IOException;
import java.util.Locale;

public class MainActivity extends Activity {
    private static final int REQUEST_PICK_IMAGE = 1204;
    private WebView webView;
    private ValueCallback<Uri[]> pendingFileCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.rgb(248, 247, 251));
        getWindow().setNavigationBarColor(Color.rgb(248, 247, 251));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);

        webView = new WebView(this);
        webView.setBackgroundColor(Color.rgb(248, 247, 251));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            getWindow().setDecorFitsSystemWindows(false);
            webView.setOnApplyWindowInsetsListener((view, insets) -> {
                Insets bars = insets.getInsets(WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars() | WindowInsets.Type.displayCutout());
                view.setPadding(bars.left, bars.top, bars.right, bars.bottom);
                return insets;
            });
        }
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(true);
        settings.setSupportZoom(false);
        settings.setBuiltInZoomControls(false);
        settings.setDisplayZoomControls(false);
        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_NEVER_ALLOW);
        settings.setJavaScriptCanOpenWindowsAutomatically(false);
        webView.addJavascriptInterface(new LamaaBridge(), "LamaaAndroid");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Uri uri = request.getUrl();
                if (uri != null && ("http".equals(uri.getScheme()) || "https".equals(uri.getScheme()))) {
                    try { startActivity(new Intent(Intent.ACTION_VIEW, uri)); } catch (Exception ignored) {}
                    return true;
                }
                return false;
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                if (pendingFileCallback != null) pendingFileCallback.onReceiveValue(null);
                pendingFileCallback = filePathCallback;
                Intent intent = fileChooserParams.createIntent();
                try { startActivityForResult(intent, REQUEST_PICK_IMAGE); }
                catch (ActivityNotFoundException e) {
                    pendingFileCallback = null;
                    Toast.makeText(MainActivity.this, "لا يوجد تطبيق لاختيار الصور", Toast.LENGTH_LONG).show();
                    return false;
                }
                return true;
            }
        });
        setContentView(webView);
        webView.loadUrl("file:///android_asset/www/index.html");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PICK_IMAGE && pendingFileCallback != null) {
            Uri[] results = null;
            if (resultCode == RESULT_OK && data != null) {
                String dataString = data.getDataString();
                if (dataString != null) results = new Uri[]{Uri.parse(dataString)};
            }
            pendingFileCallback.onReceiveValue(results);
            pendingFileCallback = null;
        }
    }

    @Override
    public void onBackPressed() {
        if (webView != null && webView.canGoBack()) webView.goBack();
        else super.onBackPressed();
    }

    private class LamaaBridge {
        @JavascriptInterface
        public void pageReady() {
            Log.i("LamaaStudio", "Web page ready");
        }

        @JavascriptInterface
        public void savePng(String dataUrl, String filename) {
            new Thread(() -> {
                try {
                    Uri uri = writePng(dataUrl, filename);
                    runOnUiThread(() -> {
                        Toast.makeText(MainActivity.this, "تم حفظ الصورة في Pictures/Lamaa", Toast.LENGTH_LONG).show();
                        Intent share = new Intent(Intent.ACTION_SEND);
                        share.setType("image/png");
                        share.putExtra(Intent.EXTRA_STREAM, uri);
                        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        try { startActivity(Intent.createChooser(share, "مشاركة تصميم لمعة")); }
                        catch (ActivityNotFoundException ex) { Toast.makeText(MainActivity.this, "تم حفظ الصورة، لكن لا توجد تطبيقات مشاركة متاحة", Toast.LENGTH_LONG).show(); }
                    });
                } catch (Exception ex) {
                    runOnUiThread(() -> Toast.makeText(MainActivity.this, "تعذر تجهيز الصورة للمشاركة", Toast.LENGTH_LONG).show());
                }
            }).start();
        }
    }

    private Uri writePng(String dataUrl, String filename) throws IOException {
        if (dataUrl == null || !dataUrl.startsWith("data:image/png;base64,")) throw new IOException("Unsupported image data");
        String encoded = dataUrl.substring(dataUrl.indexOf(',') + 1);
        byte[] bytes = Base64.decode(encoded, Base64.DEFAULT);
        if (bytes.length == 0 || bytes.length > 30 * 1024 * 1024) throw new IOException("Image size outside allowed range");
        String safeName = (filename == null ? "lamaa-design.png" : filename).replaceAll("[^A-Za-z0-9._-]", "-");
        if (!safeName.toLowerCase(Locale.ROOT).endsWith(".png")) safeName += ".png";
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DISPLAY_NAME, safeName);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
        values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/Lamaa");
        values.put(MediaStore.Images.Media.IS_PENDING, 1);
        Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        if (uri == null) throw new IOException("Could not create media entry");
        boolean complete = false;
        try (OutputStream output = getContentResolver().openOutputStream(uri, "w")) {
            if (output == null) throw new IOException("Could not open media output");
            output.write(bytes); output.flush(); complete = true;
        } finally {
            if (!complete) getContentResolver().delete(uri, null, null);
        }
        ContentValues completeValues = new ContentValues(); completeValues.put(MediaStore.Images.Media.IS_PENDING, 0);
        getContentResolver().update(uri, completeValues, null, null);
        return uri;
    }
}
