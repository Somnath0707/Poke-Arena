/**
 * PokeArena — Cinema-Grade Battle Audio Director
 * Enriches the procedural Web Audio synthesizer with deep sub-bass impacts,
 * filtered aerodynamic whooshes, electrical crackles, and stadium crowd cheers.
 */
window.PokeAudioDirector = class PokeAudioDirector {
  constructor(baseAudioEngine) {
    this.base = baseAudioEngine;
  }

  get ctx() {
    this.base?.init();
    return this.base?.ctx;
  }

  get sfxEnabled() {
    return this.base?.sfxEnabled ?? true;
  }

  /**
   * Aerodynamic whoosh / strike launch sound
   */
  playWhoosh(type = 'Normal') {
    if (!this.sfxEnabled || !this.ctx) return;
    const ctx = this.ctx;
    const t = ctx.currentTime;
    try {
      // White noise buffer for whoosh
      const bufferSize = ctx.sampleRate * 0.25;
      const buffer = ctx.createBuffer(1, bufferSize, ctx.sampleRate);
      const data = buffer.getChannelData(0);
      for (let i = 0; i < bufferSize; i++) {
        data[i] = Math.random() * 2 - 1;
      }

      const noise = ctx.createBufferSource();
      noise.buffer = buffer;

      const filter = ctx.createBiquadFilter();
      filter.type = 'bandpass';
      filter.frequency.setValueAtTime(400, t);
      filter.frequency.exponentialRampToValueAtTime(1400, t + 0.12);
      filter.frequency.exponentialRampToValueAtTime(200, t + 0.24);

      const gain = ctx.createGain();
      gain.gain.setValueAtTime(0.22, t);
      gain.gain.exponentialRampToValueAtTime(0.001, t + 0.24);

      noise.connect(filter).connect(gain).connect(ctx.destination);
      noise.start(t);
      noise.stop(t + 0.25);
    } catch {}
  }

  /**
   * Sub-bass cinematic impact boom
   */
  playSubBassBoom() {
    if (!this.sfxEnabled || !this.ctx) return;
    const ctx = this.ctx;
    const t = ctx.currentTime;
    try {
      const osc = ctx.createOscillator();
      const gain = ctx.createGain();
      osc.type = 'sine';
      osc.frequency.setValueAtTime(80, t);
      osc.frequency.exponentialRampToValueAtTime(32, t + 0.45);

      gain.gain.setValueAtTime(0.55, t);
      gain.gain.exponentialRampToValueAtTime(0.001, t + 0.45);

      osc.connect(gain).connect(ctx.destination);
      osc.start(t);
      osc.stop(t + 0.46);
    } catch {}
  }

  /**
   * Stadium crowd cheer on super-effective or KO
   */
  playCrowdCheer() {
    if (!this.sfxEnabled || !this.ctx) return;
    const ctx = this.ctx;
    const t = ctx.currentTime;
    try {
      const bufferSize = ctx.sampleRate * 0.6;
      const buffer = ctx.createBuffer(1, bufferSize, ctx.sampleRate);
      const data = buffer.getChannelData(0);
      for (let i = 0; i < bufferSize; i++) {
        data[i] = Math.random() * 2 - 1;
      }

      const noise = ctx.createBufferSource();
      noise.buffer = buffer;

      const filter = ctx.createBiquadFilter();
      filter.type = 'lowpass';
      filter.frequency.setValueAtTime(700, t);
      filter.frequency.linearRampToValueAtTime(1200, t + 0.2);
      filter.frequency.exponentialRampToValueAtTime(400, t + 0.58);

      const gain = ctx.createGain();
      gain.gain.setValueAtTime(0.01, t);
      gain.gain.linearRampToValueAtTime(0.16, t + 0.15);
      gain.gain.exponentialRampToValueAtTime(0.001, t + 0.58);

      noise.connect(filter).connect(gain).connect(ctx.destination);
      noise.start(t);
      noise.stop(t + 0.6);
    } catch {}
  }
};
