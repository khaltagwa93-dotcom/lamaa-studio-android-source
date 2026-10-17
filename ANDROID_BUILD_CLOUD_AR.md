# بناء لمعة ستوديو للأندرويد: توقيع واختبار تلقائيان

أضيف مسار GitHub Actions في `.github/workflows/android-release.yml`. عندما يكون المشروع في مستودع GitHub وتُضبط أسرار التوقيع، يبني المسار ملفي APK وAAB موقّعين، يتحقق من التوقيع، ويثبت APK على محاكي Android 36، ويختبر بقاء التطبيق قيد التشغيل وعدم ظهور علامة انهيار قاتل، ويحفظ لقطة شاشة وسجل الاختبار ضمن المخرجات.

## مهم: إنشاء مفتاح الرفع خاصتك

**لا ترفع ملف keystore أو كلمات مروره داخل المستودع، ولا ترسلها في تذكرة أو رسالة.** احتفظ بنسخة احتياطية مشفرة في مكان آمن؛ ستحتاج المفتاح نفسه لإصدار تحديثات مستقبلية للمفتاح الذي تسجله في Play Console.

1. ثبّت JDK 17 أو أحدث على جهازك، وأنشئ مفتاح رفع محليًا. في Terminal / PowerShell:

```bash
keytool -genkeypair -v -keystore upload-keystore.jks -storetype JKS -alias lamaa-upload -keyalg RSA -keysize 2048 -validity 10000
```

احتفظ بكلمة مرور المخزن وكلمة مرور المفتاح، ولا تستخدم كلمة مرور ضعيفة أو تعيد استخدام كلمة مرور حساب آخر. عند مطالبتك بـ alias استخدم `lamaa-upload` أو القيمة التي قررتها.

2. أنشئ مستودع GitHub خاصًا، وارفع ملفات المشروع، بما فيها مجلد `.github/workflows`.

3. من المستودع افتح **Settings → Secrets and variables → Actions → New repository secret** وأضف الأسرار التالية:

| اسم السر | القيمة |
|---|---|
| `ANDROID_SIGNING_KEY_BASE64` | محتوى keystore بعد تحويل الملف إلى Base64 في سطر واحد |
| `ANDROID_STORE_PASSWORD` | كلمة مرور keystore |
| `ANDROID_KEY_ALIAS` | اسم alias، مثل `lamaa-upload` |
| `ANDROID_KEY_PASSWORD` | كلمة مرور المفتاح |

تحويل الملف إلى Base64 على Linux/macOS:

```bash
base64 -w 0 upload-keystore.jks
```

وعلى PowerShell في Windows:

```powershell
[Convert]::ToBase64String([IO.File]::ReadAllBytes('upload-keystore.jks'))
```

انسخ ناتج Base64 إلى السر فقط؛ لا تضع ملف keystore نفسه في المستودع.

4. افتح **Actions → Lamaa Android Release — Build, Sign and Smoke Test → Run workflow**. كما يعمل المسار تلقائيًا عند دفع تغييرات إلى فرع `main` ضمن الملفات المحددة.

5. إذا نجح المسار، افتح تشغيل workflow ثم قسم **Artifacts** ونزّل `lamaa-studio-android-release-*`. يحتوي على `app-release.aab` المناسب للتحميل في Play Console و`app-release.apk` للاختبار اليدوي، بالإضافة إلى تقرير الاختبار ولقطة شاشة المحاكي عند توفرهما، وتقرير البناء وبصمات SHA-256 للتحقق من الملفات.

## ما الذي يتحقق منه المسار؟

- صحة صياغة JavaScript وملف Manifest، وتطابق أصول WebView مع ملفات الويب الأصلية.
- بناء Gradle وAndroid lint واختبارات وحدة Gradle المتاحة.
- التحقق من توقيع APK بواسطة `apksigner`، ومن توقيع AAB بواسطة `jarsigner`، وإرفاق بصمات SHA-256 للملفين الناتجين.
- تثبيت APK على محاكي Android 36، وتشغيل `MainActivity`، والتحقق من بقاء عملية التطبيق وعدم رصد علامة انهيار قاتل، وحفظ لقطة شاشة.

اختبار المحاكي هو اختبار دخاني (smoke test)، وليس ضمانًا لخلو التطبيق من كل العيوب أو اختبارًا شاملًا على أجهزة فعلية. راجع سجل الـ Actions قبل رفع الإصدار للإنتاج، ثم استخدم اختبارًا داخليًا في Play Console.

## حالة البيئة الحالية

هذا المسار مضاف إلى المصدر لكنه لا يصبح تنفيذًا ناجحًا إلا بعد رفع المصدر إلى مستودع GitHub وضبط الأسرار وتشغيل الـ workflow. البيئة التي أُنشئ فيها هذا الملف لا تحتوي على Android SDK أو Gradle أو apksigner ولا تسمح بتنزيلها مباشرة؛ لذلك لا أدّعي أن APK/AAB موقّع قد خرج منها.
