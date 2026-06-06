const $  = (sel, ctx = document) => ctx.querySelector(sel);
const $$ = (sel, ctx = document) => [...ctx.querySelectorAll(sel)];

const Store = {
  get:    key        => JSON.parse(localStorage.getItem(key) || 'null'),
  set:    (key, val) => localStorage.setItem(key, JSON.stringify(val)),
  remove: key        => localStorage.removeItem(key),
};

const page = document.body.dataset.page || '';

// ==================== FUNGSI GLOBAL ====================
function updateProgress(step, total) {
  const pct   = Math.round((step / total) * 100);
  const fill  = $('.progress-fill');
  const label = $('.progress-pct');
  if (fill)  fill.style.width = pct + '%';
  if (label) label.textContent = pct + '%';
}

function getQuizData() {
  return Store.get('gm_quiz') || {};
}

function saveQuizData(key, value) {
  const data = getQuizData();
  data[key] = value;
  Store.set('gm_quiz', data);
}

function isQuizComplete() {
  const data = getQuizData();
  return !!(data.mood && data.budget && data.genres && data.genres.length && data.spec);
}

function initNextStep(validate, href) {
  const btn = $('#btn-next');
  if (!btn) return;
  btn.addEventListener('click', (e) => {
    e.preventDefault(); 
    if (validate && !validate()) return;
    window.location.href = href;
  });
}

// ==================== HALAMAN LOGIN ====================
if (page === 'login') {
  window.handleGoogleResponse = (response) => {
    fetch('/api/v1/auth/google', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ idToken: response.credential })
    })
    .then(res => {
      if (!res.ok) throw new Error('Gagal verifikasi di server');
      return res.json();
    })
    .then(data => {
      Store.set('jwt_token', data.token);
      alert('Login Berhasil!');
      window.location.href = '/home'; 
    })
    .catch(err => {
      console.error(err);
      alert('Terjadi kesalahan saat login.');
    });
  };

  window.onload = () => {
    google.accounts.id.initialize({
      client_id: "1010767334798-p2inesser699mfc3eehp4uo102omod0t.apps.googleusercontent.com",
      callback: handleGoogleResponse
    });
    google.accounts.id.renderButton(
      document.getElementById("google-btn-container"),
      { theme: "filled_black", size: "large", shape: "pill", text: "signin_with" }
    );
  };

  const btnEmail  = $('#btn-email');
  const emailInput = $('#email');
  if (btnEmail) {
    btnEmail.addEventListener('click', () => {
      const email = emailInput?.value.trim();
      if (!email || !email.includes('@')) {
        emailInput?.classList.add('input-error');
        emailInput?.focus();
        return;
      }
      Store.set('gm_user', { email });
      window.location.href = '/home';
    });
  }
}

// ==================== HALAMAN HOME ====================
if (page === 'home') {
  $$('.mood-tag').forEach(tag => {
    tag.addEventListener('click', () => {
      $$('.mood-tag').forEach(t => t.classList.remove('active'));
      tag.classList.add('active');
    });
  });

  const startBtn = $('#btn-start');
  if (startBtn) {
    startBtn.addEventListener('click', () => {
      if (isQuizComplete()) {
        window.location.href = '/rekomendasi'; 
      } else {
        window.location.href = '/q1'; 
      }
    });
  }
}

// ==================== STEP 1 (Q1) ====================
if (page === 'mm-step1') {
  if (isQuizComplete()) {
    window.location.href = '/rekomendasi';
  } else {
    updateProgress(1, 4);
    $$('.mm-option').forEach(opt => {
      opt.addEventListener('click', () => {
        $$('.mm-option').forEach(o => o.classList.remove('active'));
        opt.classList.add('active');
        saveQuizData('mood', opt.dataset.value);
      });
    });

    const prev = getQuizData().mood;
    if (prev) {
      $$('.mm-option').forEach(o => {
        if (o.dataset.value === prev) o.classList.add('active');
      });
    }

    initNextStep(
      () => {
        const sel = $('.mm-option.active');
        if (!sel) { alert('Pilih kondisi psikologismu terlebih dahulu.'); return false; }
        return true;
      },
      '/q2' 
    );
  }
}

// ==================== STEP 2 (Q2) ====================
if (page === 'mm-step2') {
  updateProgress(2, 4);

  $$('.mm-chip').forEach(chip => {
    chip.addEventListener('click', () => {
      $$('.mm-chip').forEach(c => c.classList.remove('active'));
      chip.classList.add('active');
      saveQuizData('budget', chip.dataset.value);
    });
  });

  const ftp = $('#include-f2p');
  if (ftp) {
    ftp.checked = getQuizData().includeF2P || false;
    ftp.addEventListener('change', () => saveQuizData('includeF2P', ftp.checked));
  }

  const prev = getQuizData().budget;
  if (prev) {
    $$('.mm-chip').forEach(c => {
      if (c.dataset.value === prev) c.classList.add('active');
    });
  }

  initNextStep(
    () => {
      if (!$('.mm-chip.active')) { alert('Pilih batasan budgetmu terlebih dahulu.'); return false; }
      return true;
    },
    '/q3' 
  );
}

// ==================== STEP 3 (Q3) ====================
if (page === 'mm-step3') {
  updateProgress(3, 4);

  $$('.mm-genre').forEach(g => {
    g.addEventListener('click', () => {
      g.classList.toggle('active');
      const selected = $$('.mm-genre.active').map(el => el.dataset.value);
      saveQuizData('genres', selected);
    });
  });

  const prev = getQuizData().genres || [];
  $$('.mm-genre').forEach(g => {
    if (prev.includes(g.dataset.value)) g.classList.add('active');
  });

  initNextStep(
    () => {
      if (!$$('.mm-genre.active').length) { alert('Pilih setidaknya satu genre.'); return false; }
      return true;
    },
    '/q4' 
  );
}

// ==================== STEP 4 (Q4) ====================
if (page === 'mm-step4') {
  updateProgress(4, 4);

  $$('.mm-spec-item').forEach(item => {
    item.addEventListener('click', () => {
      $$('.mm-spec-item').forEach(i => i.classList.remove('active'));
      item.classList.add('active');
      saveQuizData('spec', item.dataset.value);
    });
  });

  const prev = getQuizData().spec;
  if (prev) {
    $$('.mm-spec-item').forEach(i => {
      if (i.dataset.value === prev) i.classList.add('active');
    });
  }

  const findBtn = $('#btn-find');
  if (findBtn) {
    findBtn.addEventListener('click', () => {
      if (!$('.mm-spec-item.active')) { alert('Pilih spesifikasi perangkatmu terlebih dahulu.'); return; }
      window.location.href = '/rekomendasi'; 
    });
  }
}

// ==================== HALAMAN REKOMENDASI (result) ====================
// ==================== HALAMAN REKOMENDASI (result) ====================
if (page === 'result') {
  // 1. INI DIA YANG HILANG: Ambil jawaban kuesioner user dari LocalStorage
  const quiz = getQuizData();

  // 2. Tarik data dari Spring Boot
  fetch('/api/v1/games')
    .then(res => {
      if (!res.ok) {
        throw new Error(`Server Error: ${res.status} ${res.statusText}`);
      }
      return res.json();
    })
    .then(ALL_GAMES => {
      if (!Array.isArray(ALL_GAMES)) {
         throw new Error("Data diterima, tapi formatnya bukan Array.");
      }

      if (ALL_GAMES.length === 0) {
        document.getElementById('grid-recommended').innerHTML = '<p class="text-white text-center w-100">Database game masih kosong.</p>';
        return;
      }

      const formattedGames = ALL_GAMES.map(g => ({
        id: g.gameId || g.id, 
        title: g.title || 'Unknown Title',
        price: g.price || 'Gratis',
        img: g.imageUrl || '',
        genres: g.genres ? g.genres.split(',') : [],
        spec: g.spec ? g.spec.split(',') : [],
        budget: g.budget || ''
      }));

      function filterGames(games) {
        return games.filter(g => {
          // Variabel 'quiz' sekarang aman dan bisa dibaca di sini
          const genreMatch = !quiz.genres || quiz.genres.length === 0 || g.genres.some(gen => quiz.genres.includes(gen));
          const specMatch  = !quiz.spec || g.spec.includes(quiz.spec);
          return genreMatch && specMatch;
        });
      }

      const recommended = filterGames(formattedGames).slice(0, 6);
      const others = formattedGames.filter(g => !recommended.some(r => r.id === g.id)).slice(0, 6);

      if (recommended.length === 0 && others.length === 0) {
        document.getElementById('grid-recommended').innerHTML = '<p class="text-white text-center w-100">Tidak ada game yang sesuai dengan kriteria kuesioner.</p>';
      } else {
        renderGrid('grid-recommended', recommended);
        renderGrid('grid-others', others);
      }
    })
    .catch(err => {
      console.error("Gagal mengambil data game:", err);
      const container = document.getElementById('grid-recommended');
      if (container) container.innerHTML = `<p class="text-danger text-center w-100 mt-4">${err.message}</p>`;
    });

  // 3. Fungsi Pembuat Kartu (Card)
  function buildCard(game) {
    const favs = Store.get('gm_favorites') || [];
    const isFav = favs.includes(game.id);
    return `
      <div class="game-card" data-id="${game.id}">
        <img class="game-card-img" src="${game.img}" alt="${game.title}" onerror="this.style.background='#1a1d27';this.removeAttribute('src')" />
        <button class="game-card-fav ${isFav ? 'active' : ''}" data-id="${game.id}" title="Tambah ke favorit">&#9825;</button>
        <div class="game-card-body">
          <div class="game-card-title">${game.title}</div>
          <div class="game-card-price">${game.price}</div>
          <a href="/detail?id=${game.id}" class="btn btn-primary btn-sm btn-full">Lihat Detail</a> 
        </div>
      </div>`;
  }

  // 4. Fungsi Render ke HTML
  function renderGrid(containerId, games) {
    const el = document.getElementById(containerId);
    if (!el) return;
    el.innerHTML = games.map(buildCard).join('');
  }

  // 5. Event Listener untuk Tombol Favorit
  document.addEventListener('click', e => {
    const btn = e.target.closest('.game-card-fav');
    if (!btn) return;
    const id   = parseInt(btn.dataset.id);
    let favs   = Store.get('gm_favorites') || [];
    if (favs.includes(id)) {
      favs = favs.filter(f => f !== id);
      btn.classList.remove('active');
    } else {
      favs.push(id);
      btn.classList.add('active');
    }
    Store.set('gm_favorites', favs);
  });

  // 6. Tombol Ulangi Kuesioner
  const retryBtn = document.getElementById('btn-retry');
  if (retryBtn) retryBtn.addEventListener('click', () => {
    Store.remove('gm_quiz');
    window.location.href = '/q1'; 
  });
}
// ==================== HALAMAN DETAIL ====================
if (page === 'detail') {
    const params = new URLSearchParams(location.search);
    const id = params.get('id');

    fetch(`/api/v1/games`) // Tarik semua, lalu cari by ID
    .then(res => res.json())
    .then(data => {
        const game = data.find(g => g.gameId == id);
        if (game) {
            document.getElementById('game-title').textContent = game.title;
            document.getElementById('game-dev').textContent = 'DEVELOPER: ' + game.developer;
            document.getElementById('game-release').textContent = 'RILIS: ' + (game.releaseDate || '-');
            document.getElementById('game-about').textContent = game.description;
            // Tambahkan elemen lain sesuai kebutuhan
        }
    })
      const favBtn = $('#btn-fav');
      if (favBtn) {
        let favs = Store.get('gm_favorites') || [];
        const updateFavBtn = () => {
          const isFav = favs.includes(id);
          favBtn.textContent = isFav ? '★ Tersimpan di Favorit' : '♡ Tambahkan ke Favorit';
          favBtn.classList.toggle('btn-gold', isFav);
          favBtn.classList.toggle('btn-outline', !isFav);
        };
        updateFavBtn();
        favBtn.addEventListener('click', () => {
          favs = Store.get('gm_favorites') || [];
          if (favs.includes(id)) favs = favs.filter(f => f !== id);
          else favs.push(id);
          Store.set('gm_favorites', favs);
          updateFavBtn();
        });
      }
    }



// ==================== HALAMAN FAVORITE ====================
if (page === 'favorite') {
  const quizData = getQuizData();
  
  if (!quizData.mood) {
    alert('Anda harus memulai quiz terlebih dahulu sebelum mengakses halaman Favorite.');
    window.location.href = '/q1'; 
  } else {
    const SLOT_COUNT = 8;

    fetch('/api/v1/games')
      .then(res => res.json())
      .then(ALL_GAMES => {
        function renderFav() {
          const favIds = Store.get('gm_favorites') || [];
          const favGames = favIds.map(id => ALL_GAMES.find(g => g.gameId === id)).filter(Boolean);

          const countEl = $('#fav-count');
          if (countEl) countEl.textContent = favGames.length;

          const grid = $('#fav-grid');
          if (!grid) return;

          let html = '';
          for (let i = 0; i < SLOT_COUNT; i++) {
            const g = favGames[i];
            if (g) {
              html += `
                <div class="fav-slot filled" data-id="${g.gameId}">
                  <img src="${g.imageUrl}" alt="${g.title}" onerror="this.style.opacity=0" />
                  <button class="fav-slot-del" data-id="${g.gameId}" title="Hapus dari favorit">✕</button>
                </div>`;
            } else {
              html += `<div class="fav-slot empty"></div>`;
            }
          }
          grid.innerHTML = html;

          $$('.fav-slot-del').forEach(btn => {
            btn.addEventListener('click', e => {
              e.stopPropagation();
              const id = parseInt(btn.dataset.id);
              let favs = Store.get('gm_favorites') || [];
              Store.set('gm_favorites', favs.filter(f => f !== id));
              renderFav(); 
            });
          });

          $$('.fav-slot.filled').forEach(slot => {
            slot.addEventListener('click', e => {
              if (e.target.closest('.fav-slot-del')) return;
              window.location.href = `/detail?id=${slot.dataset.id}`; 
            });
          });
        }
        renderFav(); 
      })
      .catch(err => console.error("Gagal mengambil data favorit:", err));

    const updateBtn = $('#btn-update');
    if (updateBtn) {
      updateBtn.addEventListener('click', () => {
        Store.remove('gm_quiz');
        window.location.href = '/q1'; 
      });
    }

    const loadMoreBtn = $('#btn-load-more');
    if (loadMoreBtn) {
      loadMoreBtn.addEventListener('click', () => {
        alert('Semua favorit sudah ditampilkan.');
      });
    }
  }
}

// ==================== LOGOUT ====================
$$('.btn-logout').forEach(btn => {
  btn.addEventListener('click', () => {
    Store.remove('jwt_token'); 
    Store.remove('gm_user');
    window.location.href = '/login'; 
  });
});