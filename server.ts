import express from "express";
import path from "path";
import fs from "fs";
import { createServer as createViteServer } from "vite";
import JSZip from "jszip";

async function startServer() {
  const app = express();
  const PORT = 3000;

  app.use(express.json());

  // API routes
  app.get("/api/health", (_req, res) => {
    res.json({ status: "ok", app: "Liquid Glass Video Player" });
  });

  // Get project files tree
  app.get("/api/project/tree", (_req, res) => {
    const rootDir = path.join(process.cwd(), "VideoPlayer");
    const result: Array<{ path: string; name: string; size: number; isDir: boolean }> = [];

    function scan(dir: string, relBase = "") {
      if (!fs.existsSync(dir)) return;
      const entries = fs.readdirSync(dir, { withFileTypes: true });
      for (const entry of entries) {
        if (entry.name === ".gradle" || entry.name === "build") continue;
        const relPath = path.join(relBase, entry.name);
        const fullPath = path.join(dir, entry.name);
        if (entry.isDirectory()) {
          result.push({ path: relPath, name: entry.name, size: 0, isDir: true });
          scan(fullPath, relPath);
        } else {
          const stats = fs.statSync(fullPath);
          result.push({ path: relPath, name: entry.name, size: stats.size, isDir: false });
        }
      }
    }

    scan(rootDir);
    res.json({ files: result });
  });

  // Get specific file content
  app.get("/api/project/content", (req, res) => {
    const filePath = req.query.path as string;
    if (!filePath) {
      return res.status(400).json({ error: "Missing path parameter" });
    }
    const safePath = path.normalize(filePath).replace(/^(\.\.[\/\\])+/, "");
    const fullPath = path.join(process.cwd(), "VideoPlayer", safePath);

    if (!fs.existsSync(fullPath) || fs.statSync(fullPath).isDirectory()) {
      return res.status(404).json({ error: "File not found" });
    }

    try {
      const content = fs.readFileSync(fullPath, "utf-8");
      res.json({ path: safePath, content });
    } catch {
      res.status(500).json({ error: "Could not read file" });
    }
  });

  // Download complete Android Studio project as .zip
  app.get("/api/project/download-zip", async (_req, res) => {
    const rootDir = path.join(process.cwd(), "VideoPlayer");
    const zip = new JSZip();

    function addDirToZip(dir: string, zipFolder: JSZip) {
      if (!fs.existsSync(dir)) return;
      const entries = fs.readdirSync(dir, { withFileTypes: true });
      for (const entry of entries) {
        if (entry.name === ".gradle" || entry.name === "build") continue;
        const fullPath = path.join(dir, entry.name);
        if (entry.isDirectory()) {
          const sub = zipFolder.folder(entry.name);
          if (sub) addDirToZip(fullPath, sub);
        } else {
          const content = fs.readFileSync(fullPath);
          zipFolder.file(entry.name, content);
        }
      }
    }

    const mainFolder = zip.folder("VideoPlayer")!;
    addDirToZip(rootDir, mainFolder);

    const buffer = await zip.generateAsync({ type: "nodebuffer", compression: "DEFLATE" });
    res.setHeader("Content-Disposition", "attachment; filename=LiquidGlassVideoPlayer-Android.zip");
    res.setHeader("Content-Type", "application/zip");
    res.send(buffer);
  });

  // Check APK build status
  app.get("/api/build/status", (_req, res) => {
    const apkPath = path.join(process.cwd(), "VideoPlayer/app/build/outputs/apk/debug/app-debug.apk");
    const exists = fs.existsSync(apkPath);
    let size = 0;
    if (exists) {
      size = fs.statSync(apkPath).size;
    }
    res.json({
      ready: exists,
      size,
      filename: "app-debug.apk",
      path: exists ? apkPath : null
    });
  });

  // Download APK
  app.get("/api/build/apk", (_req, res) => {
    const apkPath = path.join(process.cwd(), "VideoPlayer/app/build/outputs/apk/debug/app-debug.apk");
    if (!fs.existsSync(apkPath)) {
      return res.status(404).json({ error: "APK is currently building or not yet compiled" });
    }
    res.setHeader("Content-Disposition", "attachment; filename=app-debug.apk");
    res.setHeader("Content-Type", "application/vnd.android.package-archive");
    res.sendFile(apkPath);
  });

  // Vite middleware setup
  if (process.env.NODE_ENV !== "production") {
    const vite = await createViteServer({
      server: { middlewareMode: true },
      appType: "spa",
    });
    app.use(vite.middlewares);
  } else {
    const distPath = path.join(process.cwd(), "dist");
    app.use(express.static(distPath));
    app.get("*", (_req, res) => {
      res.sendFile(path.join(distPath, "index.html"));
    });
  }

  app.listen(PORT, "0.0.0.0", () => {
    console.log(`Server running on http://0.0.0.0:${PORT}`);
  });
}

startServer();
