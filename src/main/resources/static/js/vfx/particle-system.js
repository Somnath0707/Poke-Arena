/**
 * PokeArena — Cinema-Grade Particle Physics Engine
 * High-performance 60FPS Canvas 2D particle simulation with physics,
 * gravity, floor collisions, additive blending, and multi-shape rendering.
 */
window.PokeParticleSystem = class PokeParticleSystem {
  constructor(canvas) {
    this.canvas = canvas;
    this.ctx = canvas.getContext('2d', { alpha: true });
    this.particles = [];
    this.maxParticles = 600;
    this.timeScale = 1.0;
  }

  resize(w, h) {
    this.canvas.width = w;
    this.canvas.height = h;
  }

  clear() {
    this.particles = [];
  }

  /**
   * Spawns a single particle
   */
  emit(opts) {
    if (this.particles.length >= this.maxParticles) {
      // Evict oldest particle if capped
      this.particles.shift();
    }
    const p = {
      x: opts.x || 0,
      y: opts.y || 0,
      vx: opts.vx || 0,
      vy: opts.vy || 0,
      ax: opts.ax || 0,
      ay: opts.ay !== undefined ? opts.ay : 0, // gravity
      drag: opts.drag || 0.98,
      size: opts.size || 4,
      endSize: opts.endSize !== undefined ? opts.endSize : opts.size || 4,
      color: opts.color || '#ffffff',
      endColor: opts.endColor || opts.color || '#ffffff',
      alpha: opts.alpha !== undefined ? opts.alpha : 1.0,
      endAlpha: opts.endAlpha !== undefined ? opts.endAlpha : 0,
      rotation: opts.rotation || 0,
      vRot: opts.vRot || 0,
      shape: opts.shape || 'circle', // circle, smoke, spark, shard, ring, debris
      blend: opts.blend || 'lighter', // lighter (additive glow) or source-over
      life: 0,
      maxLife: opts.life || 30, // in frames
      floorY: opts.floorY !== undefined ? opts.floorY : null,
      bounce: opts.bounce || 0.4,
      points: opts.points || null,
      innerRadius: opts.innerRadius || 0
    };
    this.particles.push(p);
    return p;
  }

  /**
   * Spawns an explosion of particles (shockwave + sparks + smoke + debris)
   */
  explode(x, y, color = '#f59e0b', count = 35, speed = 6) {
    // 1. Shockwave ring
    this.emit({
      x, y,
      size: 10,
      endSize: 120 + speed * 10,
      color: '#ffffff',
      endColor: color,
      alpha: 0.9,
      endAlpha: 0,
      life: 18,
      shape: 'ring',
      blend: 'lighter'
    });

    // 2. High-speed sparks
    for (let i = 0; i < count; i++) {
      const angle = Math.random() * Math.PI * 2;
      const spd = (0.5 + Math.random() * 1.5) * speed;
      this.emit({
        x, y,
        vx: Math.cos(angle) * spd,
        vy: Math.sin(angle) * spd - (Math.random() * 2),
        ay: 0.18, // gravity
        size: 2 + Math.random() * 4,
        endSize: 0.5,
        color: Math.random() > 0.3 ? '#ffffff' : color,
        endColor: color,
        alpha: 1.0,
        endAlpha: 0,
        life: 20 + Math.random() * 25,
        shape: 'spark',
        blend: 'lighter'
      });
    }

    // 3. Smoke puffs
    const smokeCount = Math.floor(count * 0.4);
    for (let i = 0; i < smokeCount; i++) {
      const angle = Math.random() * Math.PI * 2;
      const spd = Math.random() * (speed * 0.4);
      this.emit({
        x: x + (Math.random() - 0.5) * 20,
        y: y + (Math.random() - 0.5) * 20,
        vx: Math.cos(angle) * spd,
        vy: Math.sin(angle) * spd - 0.5,
        drag: 0.94,
        size: 14 + Math.random() * 16,
        endSize: 35 + Math.random() * 25,
        color: '#2a3033',
        endColor: '#121415',
        alpha: 0.45,
        endAlpha: 0,
        rotation: Math.random() * Math.PI * 2,
        vRot: (Math.random() - 0.5) * 0.05,
        life: 30 + Math.random() * 25,
        shape: 'smoke',
        blend: 'source-over'
      });
    }

    // 4. Bouncing rock debris
    const debrisCount = Math.floor(count * 0.25);
    const groundY = y + 35;
    for (let i = 0; i < debrisCount; i++) {
      const angle = -Math.PI * 0.15 - Math.random() * Math.PI * 0.7;
      const spd = 3 + Math.random() * (speed * 0.9);
      this.emit({
        x, y,
        vx: Math.cos(angle) * spd,
        vy: Math.sin(angle) * spd,
        ay: 0.35, // strong gravity
        size: 3 + Math.random() * 5,
        endSize: 2,
        color: '#78716c',
        endColor: '#44403c',
        alpha: 0.9,
        endAlpha: 0.2,
        rotation: Math.random() * Math.PI * 2,
        vRot: (Math.random() - 0.5) * 0.3,
        floorY: groundY + (Math.random() - 0.5) * 15,
        bounce: 0.45,
        life: 45 + Math.random() * 20,
        shape: 'debris',
        blend: 'source-over'
      });
    }
  }

  /**
   * Update and render all particles
   */
  updateAndRender() {
    const ctx = this.ctx;
    const ts = this.timeScale;

    for (let i = this.particles.length - 1; i >= 0; i--) {
      const p = this.particles[i];

      // Physics update
      p.vx = (p.vx + p.ax * ts) * Math.pow(p.drag, ts);
      p.vy = (p.vy + p.ay * ts) * Math.pow(p.drag, ts);
      p.x += p.vx * ts;
      p.y += p.vy * ts;
      p.rotation += p.vRot * ts;

      // Floor bounce check
      if (p.floorY !== null && p.y >= p.floorY && p.vy > 0) {
        p.y = p.floorY;
        p.vy = -p.vy * p.bounce;
        p.vx *= 0.7; // friction
        if (Math.abs(p.vy) < 0.5) {
          p.vy = 0;
          p.ay = 0;
        }
      }

      p.life += ts;
      const progress = Math.min(1.0, p.life / p.maxLife);

      if (progress >= 1.0) {
        this.particles.splice(i, 1);
        continue;
      }

      // Interpolate current size and alpha
      const curSize = p.size + (p.endSize - p.size) * progress;
      const curAlpha = Math.max(0, p.alpha + (p.endAlpha - p.alpha) * progress);

      ctx.save();
      ctx.globalCompositeOperation = p.blend;
      ctx.globalAlpha = curAlpha;

      if (p.shape === 'circle') {
        ctx.fillStyle = p.color;
        ctx.beginPath();
        ctx.arc(p.x, p.y, Math.max(0.1, curSize), 0, Math.PI * 2);
        ctx.fill();
      } else if (p.shape === 'spark') {
        // Stretched spark oriented along velocity vector
        ctx.fillStyle = p.color;
        const spd = Math.sqrt(p.vx * p.vx + p.vy * p.vy);
        const len = Math.max(curSize, spd * 2.2);
        const angle = Math.atan2(p.vy, p.vx);
        ctx.translate(p.x, p.y);
        ctx.rotate(angle);
        ctx.beginPath();
        ctx.ellipse(0, 0, len, curSize * 0.6, 0, 0, Math.PI * 2);
        ctx.fill();
      } else if (p.shape === 'ring') {
        ctx.strokeStyle = p.color;
        ctx.lineWidth = Math.max(1, (1 - progress) * 6);
        ctx.beginPath();
        ctx.arc(p.x, p.y, Math.max(1, curSize), 0, Math.PI * 2);
        ctx.stroke();
      } else if (p.shape === 'smoke') {
        // Soft radial puff
        const grad = ctx.createRadialGradient(p.x, p.y, 0, p.x, p.y, curSize);
        grad.addColorStop(0, p.color);
        grad.addColorStop(0.6, p.endColor);
        grad.addColorStop(1, 'transparent');
        ctx.fillStyle = grad;
        ctx.beginPath();
        ctx.arc(p.x, p.y, curSize, 0, Math.PI * 2);
        ctx.fill();
      } else if (p.shape === 'debris') {
        // Polygonal tumbling rock chunk
        ctx.fillStyle = p.color;
        ctx.translate(p.x, p.y);
        ctx.rotate(p.rotation);
        ctx.beginPath();
        ctx.moveTo(-curSize, -curSize * 0.7);
        ctx.lineTo(curSize * 0.8, -curSize);
        ctx.lineTo(curSize, curSize * 0.6);
        ctx.lineTo(-curSize * 0.4, curSize);
        ctx.closePath();
        ctx.fill();
      } else if (p.shape === 'shard') {
        // Sharp crystal shard (for ice/rock/glass)
        ctx.fillStyle = p.color;
        ctx.translate(p.x, p.y);
        ctx.rotate(p.rotation);
        ctx.beginPath();
        ctx.moveTo(0, -curSize * 1.6);
        ctx.lineTo(curSize * 0.6, 0);
        ctx.lineTo(0, curSize * 1.6);
        ctx.lineTo(-curSize * 0.6, 0);
        ctx.closePath();
        ctx.fill();
      }

      ctx.restore();
    }
  }
};
