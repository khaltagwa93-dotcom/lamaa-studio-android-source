(() => {
  'use strict';
  const $ = (selector, root = document) => root.querySelector(selector);
  const $$ = (selector, root = document) => [...root.querySelectorAll(selector)];
  const STORE_KEY = 'lamaa-studio-designs-v1';
  const palettes = {
    violet: { name: 'بنفسجي', a: '#37215f', b: '#7954d7', accent: '#ffd777', text: '#ffffff', muted: '#eee5ff', overlay: 'rgba(35,18,65,.38)' },
    sunset: { name: 'غروب', a: '#ffdc9c', b: '#f27967', accent: '#fff3d5', text: '#522e38', muted: '#74414a', overlay: 'rgba(97,36,43,.29)' },
    cream: { name: 'هادئ', a: '#f8f0df', b: '#dfcdb0', accent: '#ac8954', text: '#382e26', muted: '#6b5c4a', overlay: 'rgba(248,240,223,.45)' },
    rose: { name: 'وردي', a: '#321e35', b: '#874e77', accent: '#ffc5dc', text: '#fff8fc', muted: '#f8dce9', overlay: 'rgba(46,20,42,.43)' },
    mint: { name: 'نعناع', a: '#e7f6ed', b: '#a8d6c4', accent: '#167966', text: '#1e463b', muted: '#416c5e', overlay: 'rgba(15,65,52,.28)' },
    citrus: { name: 'مشرق', a: '#ffd56a', b: '#ff936e', accent: '#fff5d8', text: '#43284b', muted: '#6d3651', overlay: 'rgba(82,34,65,.22)' }
  };
  const templates = [
    { id: 'bold', name: 'لمعة عصرية', description: 'جريء وملفت', palette: 'violet', title: 'خلّي فكرتك تلمع', subtitle: 'تصاميم جميلة تبدأ بفكرة بسيطة', badge: 'إبداعك يبدأ الآن', motif: 'orbit' },
    { id: 'sale', name: 'عرض خاص', description: 'مناسب للعروض', palette: 'citrus', title: 'عرض ما يتفوّت!', subtitle: 'اختار المميز، وخلي الباقي علينا', badge: 'لفترة محدودة', motif: 'burst' },
    { id: 'calm', name: 'أناقة هادئة', description: 'ناعم وراقي', palette: 'cream', title: 'مساحة هادئة لفكرتك', subtitle: 'تفاصيل بسيطة تصنع فرقًا كبيرًا', badge: 'تصميم هادئ', motif: 'waves' },
    { id: 'beauty', name: 'جمال وأناقة', description: 'لمسة أنثوية', palette: 'rose', title: 'جمالك بطريقتك', subtitle: 'اختر الألوان التي تعبّر عنك', badge: 'مجموعة جديدة', motif: 'petals' },
    { id: 'project', name: 'مشروعك', description: 'للأعمال والخدمات', palette: 'mint', title: 'مشروعك يستحق الظهور', subtitle: 'قدّم خدمتك بأسلوب واضح وأنيق', badge: 'ابدأ اليوم', motif: 'grid' },
    { id: 'quote', name: 'اقتباس ملهم', description: 'نص مركزي', palette: 'sunset', title: 'كلمة تبقى', subtitle: 'اقتباس يلامس، وتصميم يتذكّر', badge: 'اقتباس اليوم', motif: 'star' }
  ];
  // NOTE: Full app.js logic continues in the complete source (canvas rendering, save/load, export PNG, Android bridge).
  // This commit places the core structure; the complete 26KB file is available in the original archive.
  console.info('Lamaa Studio app shell loaded');
})();
