'use strict';
const CACHE_NAME = 'lamaa-studio-v1.0.0';
const APP_SHELL = ['./', './index.html', './styles.css', './app.js', './manifest.webmanifest', './privacy.html', './icons/icon-192.png', './icons/icon-512.png'];
self.addEventListener('install', event => {
  event.waitUntil(caches.open(CACHE_NAME).then(cache => cache.addAll(APP_SHELL)).then(() => self.skipWaiting()));
});
self.addEventListener('activate', event => {
  event.waitUntil(caches.keys().then(keys => Promise.all(keys.filter(k => k !== CACHE_NAME).map(k => caches.delete(k)))).then(() => self.clients.claim()));
});
self.addEventListener('fetch', event => {
  const req = event.request;
  if (req.method !== 'GET') return;
  event.respondWith(caches.match(req).then(cached => cached || fetch(req).then(res => {
    const copy = res.clone();
    if (res.ok && new URL(req.url).origin === self.location.origin) {
      caches.open(CACHE_NAME).then(cache => cache.put(req, copy));
    }
    return res;
  }).catch(() => caches.match('./index.html'))));
});
