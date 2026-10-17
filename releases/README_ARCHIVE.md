# الأرشيف الأصلي الكامل (ZIP)

ملف `lamaa-studio-android-source.zip` الأصلي (~151 KB) مرفوع هنا على شكل Base64 مقسّم إلى 3 أجزاء بسبب قيود رفع الملفات الثنائية عبر واجهة GitHub المتصلة.

## إعادة تجميع الـ ZIP محليًا

### Linux / macOS / WSL
```bash
cat releases/lamaa-studio-android-source.zip.part0.b64 \
    releases/lamaa-studio-android-source.zip.part1.b64 \
    releases/lamaa-studio-android-source.zip.part2.b64 \
  | base64 -d > lamaa-studio-android-source.zip

unzip -l lamaa-studio-android-source.zip   # للتحقق
```

### Windows (PowerShell)
```powershell
$parts = 0..2 | ForEach-Object { Get-Content "releases/lamaa-studio-android-source.zip.part$_.b64" -Raw }
$bytes = [Convert]::FromBase64String(($parts -join ""))
[IO.File]::WriteAllBytes("lamaa-studio-android-source.zip", $bytes)
```

بعد فك الضغط ستحصل على الشجرة الكاملة بما فيها:
- `app.js` و `index.html` و `styles.css` الكاملة
- أيقونات PNG
- `gradlew` / `gradlew.bat`
- workflow الكامل
- كل ملفات الـ assets داخل Android

## ملاحظة
لا ترفع ملفات keystore أو كلمات المرور إلى المستودع أبدًا.
