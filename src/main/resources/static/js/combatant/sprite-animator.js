/**
 * PokeArena — Cinema-Grade Sprite Motion & Physicality Animator
 * Replaces flat CSS translations with weighted character physics:
 * squash-and-stretch, anticipation coiling, impact deceleration,
 * turf skid recoil, dynamic facing awareness, and speed-scaled timing.
 */
window.PokeSpriteAnimator = class PokeSpriteAnimator {
  constructor() {
    this.speed = 1.0;
  }

  setSpeed(s) {
    this.speed = Math.max(0.2, parseFloat(s) || 1.0);
  }

  getScaleDuration(ms) {
    return Math.max(16, Math.round(ms / (this.speed || 1.0)));
  }

  getFacing(side) {
    const host = document.getElementById(side === 'A' ? 'combatantA' : 'combatantB');
    if (!host) return side === 'A' ? -1 : 1;
    const f = host.dataset.facing;
    if (f !== undefined && f !== null && f !== '') {
      const val = parseFloat(f);
      if (!isNaN(val)) return val;
    }
    return side === 'A' ? -1 : 1;
  }

  /**
   * Coils attacker back in anticipation of an attack
   */
  anticipate(side, durationMs = 150) {
    const host = document.getElementById(side === 'A' ? 'combatantA' : 'combatantB');
    if (!host) return;
    const wrap = host.querySelector('.sprite-orient-wrap');
    if (!wrap) return;

    const scaledDur = this.getScaleDuration(durationMs);
    wrap.style.transition = `transform ${scaledDur}ms cubic-bezier(0.25, 1, 0.5, 1)`;
    const flip = this.getFacing(side);
    const shiftX = side === 'A' ? -18 : 18;
    wrap.style.transform = `scaleX(${flip}) translate(${shiftX}px, 6px) scale(0.94, 0.94)`;
  }

  /**
   * Launches attacker toward target with stretch & acceleration
   */
  lunge(side, tier = 'STANDARD', durationMs = 220) {
    const host = document.getElementById(side === 'A' ? 'combatantA' : 'combatantB');
    if (!host) return;
    const wrap = host.querySelector('.sprite-orient-wrap');
    if (!wrap) return;

    const flip = this.getFacing(side);
    const distance = tier === 'ULTIMATE' ? 115 : tier === 'HEAVY' ? 95 : tier === 'LIGHT' ? 50 : 75;
    const targetX = side === 'A' ? distance : -distance;
    const targetY = side === 'A' ? -18 : 18;

    const scaledDur = this.getScaleDuration(durationMs);
    wrap.style.transition = `transform ${scaledDur}ms cubic-bezier(0.12, 0.9, 0.28, 1)`;
    wrap.style.transform = `scaleX(${flip}) translate(${targetX}px, ${targetY}px) scale(1.14, 0.96)`;
  }

  /**
   * Defender takes hit: squash on impact + violent knockback recoil
   */
  reactHit(side, isHeavy = false, durationMs = 320) {
    const host = document.getElementById(side === 'A' ? 'combatantA' : 'combatantB');
    if (!host) return;
    const wrap = host.querySelector('.sprite-orient-wrap');
    if (!wrap) return;

    const flip = this.getFacing(side);
    const recoilX = side === 'A' ? -35 : 35;
    const recoilY = isHeavy ? 15 : 8;

    const dur1 = this.getScaleDuration(60);
    const dur2 = this.getScaleDuration(Math.max(40, durationMs - 60));
    const dur3 = this.getScaleDuration(240);
    const delayRegain = this.getScaleDuration(durationMs + 100);

    // 1. Initial squash from impact
    wrap.style.transition = `transform ${dur1}ms ease-out`;
    wrap.style.transform = `scaleX(${flip}) translate(${recoilX * 0.4}px, ${recoilY * 0.5}px) scale(0.88, 1.15)`;

    // 2. Knockback recoil slide
    setTimeout(() => {
      wrap.style.transition = `transform ${dur2}ms cubic-bezier(0.18, 0.89, 0.32, 1.28)`;
      wrap.style.transform = `scaleX(${flip}) translate(${recoilX}px, ${recoilY}px) rotate(${side === 'A' ? -5 : 5}deg)`;
    }, dur1);

    // 3. Regain stance
    setTimeout(() => {
      wrap.style.transition = `transform ${dur3}ms ease-out`;
      wrap.style.transform = `scaleX(${flip}) translate(0px, 0px) scale(1, 1)`;
    }, delayRegain);
  }

  /**
   * Attacker rebounds back to battle podium after strike
   */
  rebound(side, durationMs = 200) {
    const host = document.getElementById(side === 'A' ? 'combatantA' : 'combatantB');
    if (!host) return;
    const wrap = host.querySelector('.sprite-orient-wrap');
    if (!wrap) return;

    const flip = this.getFacing(side);
    const scaledDur = this.getScaleDuration(durationMs);
    wrap.style.transition = `transform ${scaledDur}ms cubic-bezier(0.34, 1.56, 0.64, 1)`;
    wrap.style.transform = `scaleX(${flip}) translate(0px, 0px) scale(1, 1)`;
  }

  /**
   * Dramatic fainting sequence
   */
  async playFaint(side, durationMs = 600) {
    const host = document.getElementById(side === 'A' ? 'combatantA' : 'combatantB');
    if (!host) return;
    const wrap = host.querySelector('.sprite-orient-wrap');
    const sprite = host.querySelector('.pokemon-sprite');
    if (!wrap || !sprite) return;

    const flip = this.getFacing(side);
    const durFreeze = this.getScaleDuration(80);
    const durBuckle = this.getScaleDuration(450);
    const durFade = this.getScaleDuration(250);

    // Step 1: Shock freeze
    sprite.style.filter = 'brightness(2.5) saturate(0.2)';
    await new Promise(r => setTimeout(r, durFreeze));

    // Step 2: Desaturation and knees buckling
    sprite.style.transition = `filter ${this.getScaleDuration(350)}ms ease-out`;
    sprite.style.filter = 'grayscale(0.85) brightness(0.6)';
    wrap.style.transition = `transform ${durBuckle}ms cubic-bezier(0.55, 0.055, 0.675, 0.19)`;
    wrap.style.transform = `scaleX(${flip}) translate(${side === 'A' ? -15 : 15}px, 35px) rotate(${side === 'A' ? -18 : 18}deg) scale(0.85, 0.65)`;

    await new Promise(r => setTimeout(r, durBuckle));

    // Step 3: Fade to near invisible
    sprite.style.transition = `opacity ${this.getScaleDuration(300)}ms ease-out`;
    sprite.style.opacity = '0.1';
    await new Promise(r => setTimeout(r, durFade));
  }

  /**
   * Resets all transforms
   */
  reset(side) {
    const host = document.getElementById(side === 'A' ? 'combatantA' : 'combatantB');
    if (!host) return;
    const wrap = host.querySelector('.sprite-orient-wrap');
    const sprite = host.querySelector('.pokemon-sprite');
    const flip = this.getFacing(side);
    if (wrap) {
      wrap.style.transition = 'none';
      wrap.style.transform = `scaleX(${flip})`;
    }
    if (sprite) {
      sprite.style.filter = '';
      sprite.style.opacity = '1';
    }
  }
};
