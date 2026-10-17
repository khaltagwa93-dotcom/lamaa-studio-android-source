#!/usr/bin/env python3
"""Lightweight static checks for Lamaa Studio HTML/CSS/JS consistency."""
import pathlib
import sys

ROOT = pathlib.Path(__file__).resolve().parents[1]
errors = []

def must_exist(rel):
    p = ROOT / rel
    if not p.is_file():
        errors.append(f"missing file: {rel}")
    return p

for rel in ["index.html", "styles.css", "app.js", "sw.js", "manifest.webmanifest", "privacy.html"]:
    must_exist(rel)

index = (ROOT / "index.html").read_text(encoding="utf-8")
if 'src="app.js"' not in index and "src='app.js'" not in index:
    errors.append("index.html does not reference app.js")
if 'href="styles.css"' not in index and "href='styles.css'" not in index:
    errors.append("index.html does not reference styles.css")

if errors:
    for e in errors:
        print("FAIL:", e, file=sys.stderr)
    sys.exit(1)
print("html-static-check: OK")
