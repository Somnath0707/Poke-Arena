/**
 * PokeArena — Cinema-Grade 3D Battle Camera Director
 * Handles dynamic cinematic framing, Dutch angles, trauma-decay shake,
 * and multi-perspective tracking shots.
 */
window.PokeCinematicCamera = class PokeCinematicCamera {
  constructor(stageEl, arenaEl) {
    this.stage = stageEl;
    this.arena = arenaEl;
    this.motionEnabled = true;
    this.trauma = 0; // 0 to 1
    this.traumaDecay = 0.92;
    this.currentTransform = { scale: 1, x: 0, y: 0, rotZ: 0 };
    this.targetTransform = { scale: 1, x: 0, y: 0, rotZ: 0 };
    this.rafId = null;
    this.startShakeLoop();
  }

  setMotion(enabled) {
    this.motionEnabled = enabled;
    if (!enabled) this.reset();
  }

  reset() {
    this.targetTransform = { scale: 1, x: 0, y: 0, rotZ: 0 };
    this.trauma = 0;
    this.applyTransform();
    this.arena?.classList.remove('cinematic');
  }

  wide() {
    this.targetTransform = { scale: 1, x: 0, y: 0, rotZ: 0 };
    this.applyTransform();
    this.arena?.classList.remove('cinematic');
  }

  attackerWindup(side) {
    if (!this.motionEnabled) return;
    // Low angle push-in, dramatic upward tilt
    this.targetTransform = {
      scale: 1.10,
      x: side === 'A' ? 4.5 : -5.5,
      y: side === 'A' ? -3 : 4,
      rotZ: side === 'A' ? -1.5 : 1.5
    };
    this.applyTransform();
  }

  strikeTrack(side, isProjectile) {
    if (!this.motionEnabled) return;
    // Tracking pan along the attack trajectory
    this.targetTransform = {
      scale: isProjectile ? 1.07 : 1.12,
      x: side === 'A' ? -3 : 3,
      y: 1,
      rotZ: side === 'A' ? 1 : -1
    };
    this.applyTransform();
  }

  impactSnap(side, intensity = 'normal') {
    if (!this.motionEnabled) return;
    // Violent Dutch-angle punch-in
    const zoom = intensity === 'heavy' ? 1.16 : 1.11;
    this.targetTransform = {
      scale: zoom,
      x: side === 'A' ? 5 : -6,
      y: side === 'A' ? -3 : 4,
      rotZ: side === 'A' ? 2.5 : -2.5
    };
    this.applyTransform();
    this.addTrauma(intensity === 'heavy' ? 0.85 : 0.45);

    // Auto recovery to wide
    setTimeout(() => {
      this.wide();
    }, intensity === 'heavy' ? 420 : 300);
  }

  ultimate(side) {
    if (!this.motionEnabled) return;
    this.arena?.classList.add('cinematic');
    this.targetTransform = {
      scale: 1.20,
      x: side === 'A' ? 6 : -7,
      y: side === 'A' ? -5 : 6,
      rotZ: side === 'A' ? -2 : 2
    };
    this.applyTransform();
  }

  faintPull() {
    if (!this.motionEnabled) return;
    // Slow pull-back to wide
    this.targetTransform = {
      scale: 0.95,
      x: 0,
      y: 2.5,
      rotZ: 0
    };
    this.applyTransform();
  }

  focusAttacker(side) {
    this.attackerWindup(side);
  }

  focusDefender(side) {
    if (!this.motionEnabled) return;
    this.targetTransform = {
      scale: 1.08,
      x: side === 'A' ? 4 : -5,
      y: side === 'A' ? -2 : 3,
      rotZ: side === 'A' ? -1 : 1
    };
    this.applyTransform();
  }

  trackProjectile(side) {
    this.strikeTrack(side, true);
  }

  impactZoom(side) {
    this.impactSnap(side, 'normal');
  }

  dramaticPull() {
    this.faintPull();
  }

  addTrauma(amount) {
    this.trauma = Math.min(1.0, this.trauma + amount);
  }

  shake(intensity = 'normal') {
    let amount;
    if (typeof intensity === 'number') {
      amount = intensity;
    } else {
      amount = intensity === 'heavy' ? 0.85 : intensity === 'light' ? 0.35 : 0.55;
    }
    this.addTrauma(amount);
  }

  applyTransform() {
    if (!this.stage) return;
    // Smooth transition
    this.stage.style.transition = 'transform 0.28s cubic-bezier(0.22, 1, 0.36, 1)';
    const t = this.targetTransform;
    this.stage.style.transform = `scale(${t.scale}) translate(${t.x}%, ${t.y}%) rotate(${t.rotZ}deg)`;
  }

  startShakeLoop() {
    const update = () => {
      if (this.trauma > 0.02) {
        const shake = this.trauma * this.trauma; // non-linear trauma response
        const maxOffset = 18;
        const maxRot = 2.5;

        const offsetX = (Math.random() * 2 - 1) * maxOffset * shake;
        const offsetY = (Math.random() * 2 - 1) * maxOffset * shake;
        const rot = (Math.random() * 2 - 1) * maxRot * shake;

        if (this.arena) {
          this.arena.style.transform = `translate(${offsetX}px, ${offsetY}px) rotate(${rot}deg)`;
        }
        this.trauma *= this.traumaDecay;
      } else if (this.trauma > 0) {
        this.trauma = 0;
        if (this.arena) this.arena.style.transform = '';
      }
      this.rafId = requestAnimationFrame(update);
    };
    this.rafId = requestAnimationFrame(update);
  }
};
