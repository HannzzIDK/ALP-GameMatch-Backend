const $  = (sel, ctx = document) => ctx.querySelector(sel);
const $$ = (sel, ctx = document) => [...ctx.querySelectorAll(sel)];

const Store = {
  get:    key        => JSON.parse(localStorage.getItem(key) || 'null'),
  set:    (key, val) => localStorage.setItem(key, JSON.stringify(val)),
  remove: key        => localStorage.removeItem(key),
};

const page = document.body.dataset.page || '';

function getAuthHeaders() {
  const headers = { 'Content-Type': 'application/json' };
  const token = Store.get('jwt_token');
  if (token) {
    headers['Authorization'] = 'Bearer ' + token;
  } else {
    const user = Store.get('gm_user');
    if (user && user.email) {
      headers['X-User-Email'] = user.email;
    }
  }
  return headers;
}

function syncFavorites() {
  const headers = getAuthHeaders();
  if (headers['Authorization'] || headers['X-User-Email']) {
    return fetch('/api/v1/favorites', { headers })
      .then(res => {
        if (res.ok) return res.json();
        return [];
      })
      .then(favIds => {
        Store.set('gm_favorites', favIds);
      })
      .catch(err => console.error("Error syncing favorites:", err));
  }
  return Promise.resolve();
}

// Sync favorites on script load
syncFavorites();

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
    const payload = JSON.parse(atob(response.credential.split('.')[1]));
    const userEmail = payload.email;

    fetch('/api/v1/auth/google', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ idToken: response.credential })
    })
    .then(res => res.json())
    .then(data => {
      Store.set('jwt_token', data.token);

      Store.set('gm_user', { email: userEmail }); 
      
      alert('Login Berhasil!');
      window.location.href = '/home'; 
    })
    .catch(err => {
      console.error(err);
      alert('Terjadi kesalahan saat login.');
    });
  };
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
  // Event Listener untuk Tombol Favorit
  document.addEventListener('click', e => {
    const btn = e.target.closest('.game-card-fav');
    if (!btn) return;
    
    // Ambil ID game dari elemen HTML
    const gameId = parseInt(btn.dataset.id);
    
    // Ambil identitas user (email) dari LocalStorage saat login
    const user = Store.get('gm_user'); 
    if (!user || !user.email) {
      alert("Silakan login terlebih dahulu untuk menyimpan favorit!");
      return;
    }

    // Tembak API Backend kita
    fetch('/api/v1/favorites/toggle', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ 
        email: user.email, 
        gameId: gameId 
      })
    })
    .then(res => res.json())
    .then(data => {
      // Ubah warna tombol (UI) berdasarkan balasan dari Spring Boot
      if (data.status === 'added') {
        btn.classList.add('active');
      } else if (data.status === 'removed') {
        btn.classList.remove('active');
      }
    })
    .catch(err => console.error("Gagal update favorit:", err));
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
        const gameId = parseInt(id);
        let favs = Store.get('gm_favorites') || [];
        const updateFavBtn = () => {
          const isFav = favs.includes(gameId);
          favBtn.textContent = isFav ? '★ Tersimpan di Favorit' : '♡ Tambahkan ke Favorit';
          favBtn.classList.toggle('btn-gold', isFav);
          favBtn.classList.toggle('btn-outline', !isFav);
        };
        updateFavBtn();
        favBtn.addEventListener('click', () => {
          favs = Store.get('gm_favorites') || [];
          const headers = getAuthHeaders();
          const isLoggedIn = headers['Authorization'] || headers['X-User-Email'];
          if (favs.includes(gameId)) {
            favs = favs.filter(f => f !== gameId);
            if (isLoggedIn) {
              fetch(`/api/v1/favorites/${gameId}`, {
                method: 'DELETE',
                headers: headers
              }).catch(err => console.error("Error removing favorite:", err));
            }
          } else {
            favs.push(gameId);
            if (isLoggedIn) {
              fetch('/api/v1/favorites', {
                method: 'POST',
                headers: headers,
                body: JSON.stringify({ gameId: gameId })
              }).catch(err => console.error("Error adding favorite:", err));
            }
          }
          Store.set('gm_favorites', favs);
          updateFavBtn();
        });
        const user = Store.get('gm_user');
    if (!user || !user.email) {
        alert("Silakan login terlebih dahulu!");
        window.location.href = '/login';
    }

    // Panggil API beserta email user di ujung URL
    fetch(`/api/v1/favorites/${user.email}`)
      .then(res => res.json())
      .then(favGames => {
          // Render array favGames ke dalam HTML (grid)
          console.log("Data favoritku:", favGames);
          // ... lanjutkan fungsi renderGrid kamu ...
      })
      .catch(err => console.error("Gagal load favorit:", err));
      }
    }



// ==================== HALAMAN FAVORITE ====================
if (page === 'favorite') {
  const user = Store.get('gm_user');
  
  // Jika user belum login, tendang kembali ke halaman login
  if (!user || !user.email) {
    alert("Sesi telah habis. Silakan login terlebih dahulu untuk melihat Favorit Anda.");
    window.location.href = '/login'; 
  } else {
    
    // Fungsi untuk menarik dan menampilkan data dari MySQL
    function loadFavorites() {
      fetch(`/api/v1/favorites/${user.email}`)
        .then(res => res.json())
        .then(favGames => {
          
          // 1. Update teks jumlah game
          const countEl = document.getElementById('fav-count');
          if (countEl) countEl.textContent = favGames.length;

          // 2. Render grid favorit
          const grid = document.getElementById('fav-grid');
          if (!grid) return;

          let html = '';
          const SLOT_COUNT = 8; // Menampilkan 8 slot (kosong atau terisi)

          for (let i = 0; i < SLOT_COUNT; i++) {
            const g = favGames[i];
            if (g) {
              // Jika data game ada, buat card-nya
              html += `
                <div class="fav-slot filled" data-id="${g.gameId || g.id}">
                  <img src="${g.imageUrl || g.img}" alt="${g.title}" onerror="this.style.opacity=0" />
                  <button class="fav-slot-del" data-id="${g.gameId || g.id}" title="Hapus dari favorit">✕</button>
                </div>`;
            } else {
              // Jika kosong, render kotak slot kosong
              html += `<div class="fav-slot empty"></div>`;
            }
          }
          grid.innerHTML = html;

          // 3. Pasang Event Listener untuk tombol hapus (✕)
          document.querySelectorAll('.fav-slot-del').forEach(btn => {
            btn.addEventListener('click', e => {
              e.stopPropagation(); // Mencegah klik nyasar ke link detail
              const gameId = parseInt(btn.dataset.id);
              
              // Kirim perintah hapus ke Backend
              fetch('/api/v1/favorites/toggle', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ email: user.email, gameId: gameId })
              })
              .then(res => res.json())
              .then(data => {
                // Jika sukses dihapus dari MySQL, muat ulang tampilannya
                if (data.status === 'removed') {
                  loadFavorites(); 
                }
              })
              .catch(err => console.error("Gagal menghapus favorit:", err));
            });
          });

          // 4. Pasang Event Listener jika gambar diklik -> Pergi ke halaman Detail
          document.querySelectorAll('.fav-slot.filled').forEach(slot => {
            slot.addEventListener('click', e => {
              if (e.target.closest('.fav-slot-del')) return;
              window.location.href = `/detail?id=${slot.dataset.id}`; 
            });
          });

        })
        .catch(err => {
          console.error("Gagal load data favorit:", err);
          document.getElementById('fav-grid').innerHTML = `<p class="text-danger w-100 text-center">Gagal memuat data dari server.</p>`;
        });
    }

    // Jalankan fungsi load saat halaman pertama kali dibuka
    loadFavorites();

    // Fungsi tambahan untuk tombol-tombol lain di UI
    const updateBtn = document.getElementById('btn-update');
    if (updateBtn) {
      updateBtn.addEventListener('click', () => {
        Store.remove('gm_quiz'); // Reset kuesioner
        window.location.href = '/q1'; 
      });
    }

    const loadMoreBtn = document.getElementById('btn-load-more');
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