/**
 * PokeArena — Cinema-Grade Move Visual Effects Engine
 * High-end cinematic visual representations for all elemental moves.
 * Features fractal lightning, volumetric flame plumes, crystalline frost,
 * pressurized water jets, colossal energy beams, and dynamic ground debris.
 */
window.PokeMoveVFXEngine = class PokeMoveVFXEngine {
  constructor(canvas) {
    this.canvas = canvas;
    this.ctx = canvas.getContext('2d', { alpha: true });
    this.particles = new PokeParticleSystem(canvas);
    this.activeBeams = [];
    this.activeEffects = [];
    this.ambientMotes = [];
    this.initAmbientMotes();
  }

  resize() {
    if (!this.canvas || !this.canvas.parentElement) return;
    const rect = this.canvas.parentElement.getBoundingClientRect();
    this.canvas.width = rect.width;
    this.canvas.height = rect.height;
    this.particles.resize(rect.width, rect.height);
  }

  clear() {
    this.particles.clear();
    this.activeBeams = [];
    this.activeEffects = [];
  }

  initAmbientMotes() {
    this.ambientMotes = [];
    const w = this.canvas.width || 800;
    const h = this.canvas.height || 500;
    for (let i = 0; i < 30; i++) {
      this.ambientMotes.push({
        x: Math.random() * w,
        y: Math.random() * h,
        vx: (Math.random() - 0.5) * 0.3,
        vy: -0.1 - Math.random() * 0.3,
        size: 1.0 + Math.random() * 2.0,
        alpha: 0.15 + Math.random() * 0.35
      });
    }
  }

  getCenter(side) {
    let el = side === 'A' ? document.getElementById('spriteA') : document.getElementById('spriteB');
    if (!el || el.classList.contains('hidden') || el.offsetWidth === 0) {
      el = side === 'A' ? document.getElementById('combatantA') : document.getElementById('combatantB');
    }
    if (!el) return { x: side === 'A' ? 200 : 600, y: 300 };
    const arenaRect = this.canvas.getBoundingClientRect();
    const rect = el.getBoundingClientRect();
    return {
      x: rect.left - arenaRect.left + rect.width / 2,
      y: rect.top - arenaRect.top + rect.height * 0.55
    };
  }

  // =========================================================================
  // 1. ⚡ ELECTRIC: Fractal Lightning & Strobe Shock
  // =========================================================================
  castThunderbolt(from, to, tier = 'STANDARD') {
    const isHeavy = tier === 'HEAVY' || tier === 'ULTIMATE';
    const branches = isHeavy ? 4 : 2;

    // Generate main jagged lightning path + sub-branches
    for (let b = 0; b < branches; b++) {
      const points = [];
      const steps = 18;
      const startX = from.x + (Math.random() - 0.5) * 20;
      const startY = from.y + (Math.random() - 0.5) * 20;
      const endX = to.x + (Math.random() - 0.5) * (b === 0 ? 10 : 60);
      const endY = to.y + (Math.random() - 0.5) * (b === 0 ? 10 : 60);

      points.push({ x: startX, y: startY });
      for (let i = 1; i < steps; i++) {
        const t = i / steps;
        const jitter = (Math.sin(t * Math.PI) * 55 + 15) * (b === 0 ? 1 : 1.4);
        const nx = startX + (endX - startX) * t + (Math.random() - 0.5) * jitter;
        const ny = startY + (endY - startY) * t + (Math.random() - 0.5) * jitter;
        points.push({ x: nx, y: ny });
      }
      points.push({ x: endX, y: endY });

      this.activeBeams.push({
        type: 'lightning',
        points,
        color: b === 0 ? '#ffffff' : '#fef08a',
        glowColor: '#eab308',
        width: b === 0 ? (isHeavy ? 7 : 4.5) : 2.5,
        life: 1.0,
        decay: isHeavy ? 0.05 : 0.08
      });
    }

    // High voltage sparks along the lightning line
    for (let i = 0; i < (isHeavy ? 30 : 15); i++) {
      const t = Math.random();
      const sx = from.x + (to.x - from.x) * t;
      const sy = from.y + (to.y - from.y) * t;
      this.particles.emit({
        x: sx + (Math.random() - 0.5) * 30,
        y: sy + (Math.random() - 0.5) * 30,
        vx: (Math.random() - 0.5) * 8,
        vy: (Math.random() - 0.5) * 8,
        size: 2 + Math.random() * 3,
        color: '#fef08a',
        endColor: '#ca8a04',
        life: 15,
        shape: 'spark'
      });
    }
  }

  // =========================================================================
  // 2. 🔥 FIRE: Turbulent Volumetric Flame Plume
  // =========================================================================
  castFlamethrower(from, to, tier = 'STANDARD') {
    const isHeavy = tier === 'HEAVY' || tier === 'ULTIMATE';
    const angle = Math.atan2(to.y - from.y, to.x - from.x);
    const dist = Math.hypot(to.x - from.x, to.y - from.y);
    const count = isHeavy ? 55 : 35;

    for (let i = 0; i < count; i++) {
      const spd = 6 + Math.random() * 9;
      const spread = (Math.random() - 0.5) * 0.28;
      const pAngle = angle + spread;
      const isCore = Math.random() > 0.45;

      this.particles.emit({
        x: from.x + Math.cos(angle) * (i * (dist / count) * 0.3),
        y: from.y + Math.sin(angle) * (i * (dist / count) * 0.3),
        vx: Math.cos(pAngle) * spd,
        vy: Math.sin(pAngle) * spd - 0.8,
        drag: 0.96,
        size: 8 + Math.random() * 12,
        endSize: 28 + Math.random() * 24,
        color: isCore ? '#ffffff' : '#f97316',
        endColor: '#dc2626',
        alpha: 0.9,
        endAlpha: 0,
        life: 25 + Math.random() * 20,
        shape: 'smoke',
        blend: 'lighter'
      });
    }

    // Flying embers
    for (let i = 0; i < 20; i++) {
      const spd = 4 + Math.random() * 12;
      const pAngle = angle + (Math.random() - 0.5) * 0.45;
      this.particles.emit({
        x: from.x,
        y: from.y,
        vx: Math.cos(pAngle) * spd,
        vy: Math.sin(pAngle) * spd - 2,
        ay: 0.05,
        size: 2 + Math.random() * 3,
        color: '#fde047',
        endColor: '#ea580c',
        life: 35 + Math.random() * 25,
        shape: 'spark',
        blend: 'lighter'
      });
    }
  }

  // =========================================================================
  // 3. 💧 WATER: Pressurized Hydro Cannon & Cascades
  // =========================================================================
  castHydroPump(from, to, tier = 'HEAVY') {
    const angle = Math.atan2(to.y - from.y, to.x - from.x);
    const count = 45;

    // High velocity core stream
    for (let i = 0; i < count; i++) {
      const spd = 12 + Math.random() * 8;
      const spread = (Math.random() - 0.5) * 0.16;
      this.particles.emit({
        x: from.x,
        y: from.y,
        vx: Math.cos(angle + spread) * spd,
        vy: Math.sin(angle + spread) * spd,
        drag: 0.97,
        size: 10 + Math.random() * 10,
        endSize: 26 + Math.random() * 16,
        color: '#38bdf8',
        endColor: '#0284c7',
        alpha: 0.85,
        endAlpha: 0,
        life: 22 + Math.random() * 15,
        shape: 'smoke',
        blend: 'lighter'
      });
    }

    // Spray droplets with bounce
    for (let i = 0; i < 35; i++) {
      const spd = 6 + Math.random() * 14;
      const spread = (Math.random() - 0.5) * 0.5;
      this.particles.emit({
        x: from.x,
        y: from.y,
        vx: Math.cos(angle + spread) * spd,
        vy: Math.sin(angle + spread) * spd - 1.5,
        ay: 0.28,
        floorY: to.y + 30,
        bounce: 0.35,
        size: 3 + Math.random() * 3,
        color: '#e0f2fe',
        endColor: '#38bdf8',
        life: 35,
        shape: 'spark',
        blend: 'lighter'
      });
    }
  }

  // =========================================================================
  // 4. ❄️ ICE: Crystalline Shards & Frost Explosion
  // =========================================================================
  castIceBeam(from, to, tier = 'STANDARD') {
    const angle = Math.atan2(to.y - from.y, to.x - from.x);
    const count = 28;

    // Spinning crystal shards
    for (let i = 0; i < count; i++) {
      const spd = 9 + Math.random() * 8;
      const spread = (Math.random() - 0.5) * 0.22;
      this.particles.emit({
        x: from.x + (Math.random() - 0.5) * 15,
        y: from.y + (Math.random() - 0.5) * 15,
        vx: Math.cos(angle + spread) * spd,
        vy: Math.sin(angle + spread) * spd,
        rotation: Math.random() * Math.PI * 2,
        vRot: (Math.random() - 0.5) * 0.4,
        size: 5 + Math.random() * 7,
        color: '#e0f2fe',
        endColor: '#7dd3fc',
        alpha: 0.95,
        life: 28,
        shape: 'shard',
        blend: 'lighter'
      });
    }

    // Frost mist trail
    for (let i = 0; i < 20; i++) {
      const spd = 4 + Math.random() * 7;
      this.particles.emit({
        x: from.x,
        y: from.y,
        vx: Math.cos(angle) * spd,
        vy: Math.sin(angle) * spd,
        size: 12 + Math.random() * 14,
        endSize: 32,
        color: '#bae6fd',
        endColor: '#38bdf8',
        alpha: 0.4,
        endAlpha: 0,
        life: 30,
        shape: 'smoke',
        blend: 'lighter'
      });
    }
  }

  // =========================================================================
  // 5. 💥 ULTIMATE: Colossal Hyper Beam / Solar Beam
  // =========================================================================
  castHyperBeam(from, to) {
    // 1. Colossal laser beam with dual core
    this.activeBeams.push({
      type: 'laser',
      from: { x: from.x, y: from.y },
      to: { x: to.x, y: to.y },
      width: 48,
      coreWidth: 20,
      color: 'rgba(255, 255, 255, 0.95)',
      glowColor: '#ef4444',
      life: 1.0,
      decay: 0.045
    });

    // 2. Concentric shockwave rings along beam path
    for (let i = 1; i <= 4; i++) {
      const t = i / 5;
      const rx = from.x + (to.x - from.x) * t;
      const ry = from.y + (to.y - from.y) * t;
      this.particles.emit({
        x: rx, y: ry,
        size: 8,
        endSize: 60,
        color: '#ffffff',
        endColor: '#ef4444',
        life: 16,
        shape: 'ring',
        blend: 'lighter'
      });
    }

    // 3. Massive particle discharge
    this.particles.explode(to.x, to.y, '#ef4444', 50, 10);
  }

  // =========================================================================
  // 6. 🌍 GROUND / ROCK: Earthquake & Flying Debris
  // =========================================================================
  castEarthquake(from, to) {
    // Ground shockwave rings expanding across the entire stadium pitch
    const midX = (from.x + to.x) / 2;
    const midY = Math.max(from.y, to.y) + 20;

    for (let r = 0; r < 3; r++) {
      setTimeout(() => {
        this.particles.emit({
          x: midX, y: midY,
          size: 20 + r * 20,
          endSize: 220 + r * 50,
          color: '#d97706',
          endColor: '#78350f',
          alpha: 0.8,
          endAlpha: 0,
          life: 25,
          shape: 'ring',
          blend: 'source-over'
        });
      }, r * 80);
    }

    // Flying rock boulders launched upward
    for (let i = 0; i < 24; i++) {
      const posX = from.x + (to.x - from.x) * (i / 24) + (Math.random() - 0.5) * 40;
      this.particles.emit({
        x: posX,
        y: midY,
        vx: (Math.random() - 0.5) * 5,
        vy: -7 - Math.random() * 7, // launched high into air
        ay: 0.38, // gravity pulls them back down
        floorY: midY + 10,
        bounce: 0.3,
        size: 5 + Math.random() * 8,
        color: '#57534e',
        endColor: '#292524',
        rotation: Math.random() * Math.PI * 2,
        vRot: (Math.random() - 0.5) * 0.35,
        life: 50,
        shape: 'debris',
        blend: 'source-over'
      });
    }
  }

  // =========================================================================
  // Master Render Loop (called every frame)
  // =========================================================================
  render(clear = true) {
    const ctx = this.ctx;
    if (clear) ctx.clearRect(0, 0, this.canvas.width, this.canvas.height);

    // 1. Ambient stadium atmosphere motes
    ctx.save();
    ctx.fillStyle = '#ffffff';
    for (let m of this.ambientMotes) {
      m.x += m.vx;
      m.y += m.vy;
      if (m.y < 0) m.y = this.canvas.height;
      if (m.x < 0) m.x = this.canvas.width;
      if (m.x > this.canvas.width) m.x = 0;
      ctx.globalAlpha = m.alpha;
      ctx.beginPath();
      ctx.arc(m.x, m.y, m.size, 0, Math.PI * 2);
      ctx.fill();
    }
    ctx.restore();

    // 2. Render laser beams / lightning arcs
    for (let i = this.activeBeams.length - 1; i >= 0; i--) {
      const b = this.activeBeams[i];
      ctx.save();

      if (b.type === 'lightning') {
        ctx.strokeStyle = b.color;
        ctx.lineWidth = b.width * b.life;
        ctx.shadowBlur = 24;
        ctx.shadowColor = b.glowColor;
        ctx.beginPath();
        b.points.forEach((pt, idx) => {
          if (idx === 0) ctx.moveTo(pt.x, pt.y);
          else ctx.lineTo(pt.x, pt.y);
        });
        ctx.stroke();
      } else if (b.type === 'laser') {
        // Outer glow
        ctx.strokeStyle = b.glowColor;
        ctx.lineWidth = b.width * b.life;
        ctx.shadowBlur = 35;
        ctx.shadowColor = b.glowColor;
        ctx.beginPath();
        ctx.moveTo(b.from.x, b.from.y);
        ctx.lineTo(b.to.x, b.to.y);
        ctx.stroke();

        // Inner searing white core
        ctx.strokeStyle = b.color;
        ctx.lineWidth = (b.coreWidth || 16) * b.life;
        ctx.shadowBlur = 10;
        ctx.shadowColor = '#ffffff';
        ctx.beginPath();
        ctx.moveTo(b.from.x, b.from.y);
        ctx.lineTo(b.to.x, b.to.y);
        ctx.stroke();
      }

      ctx.restore();
      b.life -= b.decay;
      if (b.life <= 0) this.activeBeams.splice(i, 1);
    }

    // 3. Render physics particles (debris, smoke, sparks, shockwaves)
    this.particles.updateAndRender();
  }
};
