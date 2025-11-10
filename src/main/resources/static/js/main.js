// ===== CONFIGURATION =====
const API_BASE_URL = 'http://localhost:8080/api/running';

// ===== DOM ELEMENTS =====
const analyzeForm = document.getElementById('analyzeForm');
const distanceInput = document.getElementById('distance');
const timeInput = document.getElementById('time');
const btnText = document.getElementById('btnText');
const spinner = document.getElementById('spinner');
const submitBtn = document.getElementById('submitBtn');
const loading = document.getElementById('loading');
const errorMessage = document.getElementById('errorMessage');
const resultSection = document.getElementById('resultSection');
const historySection = document.getElementById('historySection');
const analysisContent = document.getElementById('analysisContent');
const suggestionsContent = document.getElementById('suggestionsContent');
const recentRuns = document.getElementById('recentRuns');
const refreshBtn = document.getElementById('refreshBtn');

// ===== INITIALIZATION =====
document.addEventListener('DOMContentLoaded', () => {
    loadRecentRuns();

    // Event Listeners
    analyzeForm.addEventListener('submit', handleAnalyze);
    refreshBtn.addEventListener('click', loadRecentRuns);
});

// ===== TOGGLE SECTIONS =====
function showHistory() {
    resultSection.classList.remove('active');
    historySection.classList.add('active');
    loadRecentRuns();
}

function hideHistory() {
    historySection.classList.remove('active');
}

// ===== ANALYZE RUN =====
async function handleAnalyze(event) {
    event.preventDefault();

    // Validate input
    const distance = parseFloat(distanceInput.value);
    const timeInMinutes = parseInt(timeInput.value);

    if (distance <= 0 || timeInMinutes <= 0) {
        showError('Distance og tid skal være positive tal');
        return;
    }

    // Prepare request
    const request = {
        distance: distance,
        timeInMinutes: timeInMinutes
    };

    // Show loading state
    setLoadingState(true);
    hideError();
    hideResult();
    hideHistory();

    try {
        const response = await fetch(`${API_BASE_URL}/analyze`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(request)
        });

        if (!response.ok) {
            throw new Error(`Server fejl: ${response.status}`);
        }

        const data = await response.json();
        displayResult(data);

        // Reset form
        analyzeForm.reset();

    } catch (error) {
        console.error('Fejl ved analyse:', error);
        showError('Der opstod en fejl ved analysen. Sørg for at backend kører på port 8080.');
    } finally {
        setLoadingState(false);
    }
}

// ===== DISPLAY RESULT =====
function displayResult(data) {
    // Show result section
    resultSection.classList.add('active');

    // Display analysis
    analysisContent.textContent = data.analysis || 'Ingen analyse tilgængelig';

    // Display suggestions
    suggestionsContent.textContent = data.suggestions || 'Ingen forslag tilgængelig';

    // Scroll to result smoothly
    resultSection.scrollIntoView({ behavior: 'smooth', block: 'start' });
}

// ===== LOAD RECENT RUNS =====
async function loadRecentRuns() {
    recentRuns.innerHTML = '<p class="loading-text">Henter løb...</p>';

    try {
        const response = await fetch(`${API_BASE_URL}/recent`);

        if (!response.ok) {
            throw new Error(`Server fejl: ${response.status}`);
        }

        const runs = await response.json();

        if (runs.length === 0) {
            recentRuns.innerHTML = '<p class="loading-text">Ingen løb endnu. Analysér dit første løb!</p>';
            return;
        }

        displayRecentRuns(runs);

    } catch (error) {
        console.error('Fejl ved hentning af løb:', error);
        recentRuns.innerHTML = '<p class="loading-text">Kunne ikke hente løb. Prøv igen.</p>';
    }
}

// ===== DISPLAY RECENT RUNS =====
function displayRecentRuns(runs) {
    recentRuns.innerHTML = '';

    runs.forEach(run => {
        const runCard = createRunCard(run);
        recentRuns.appendChild(runCard);
    });
}

// ===== CREATE RUN CARD =====
function createRunCard(run) {
    const card = document.createElement('div');
    card.className = 'run-card';

    // Format date
    const date = new Date(run.createdAt);
    const formattedDate = formatDate(date);

    // Calculate pace for display
    const pace = (run.timeInMinutes / run.distance).toFixed(2);

    card.innerHTML = `
        <div class="run-header">
            <div class="run-stats">
                ${run.distance} km • ${run.timeInMinutes} min
            </div>
            <button class="btn-danger" onclick="deleteRun(${run.id})">
                🗑️ Slet
            </button>
        </div>
        <div class="run-date">${formattedDate}</div>
        <div class="run-analysis">${formatAnalysisPreview(run.analysis)}</div>
    `;

    return card;
}

// ===== DELETE RUN =====
async function deleteRun(id) {
    if (!confirm('Er du sikker på at du vil slette dette løb?')) {
        return;
    }

    try {
        const response = await fetch(`${API_BASE_URL}/${id}`, {
            method: 'DELETE'
        });

        if (!response.ok) {
            throw new Error(`Server fejl: ${response.status}`);
        }

        // Reload runs
        loadRecentRuns();

    } catch (error) {
        console.error('Fejl ved sletning:', error);
        showError('Kunne ikke slette løbet. Prøv igen.');
    }
}

// ===== UTILITY FUNCTIONS =====

function setLoadingState(isLoading) {
    if (isLoading) {
        submitBtn.disabled = true;
        btnText.style.display = 'none';
        spinner.style.display = 'inline';
        loading.classList.add('active');
    } else {
        submitBtn.disabled = false;
        btnText.style.display = 'inline';
        spinner.style.display = 'none';
        loading.classList.remove('active');
    }
}

function showError(message) {
    errorMessage.textContent = message;
    errorMessage.classList.add('active');

    // Auto-hide after 5 seconds
    setTimeout(() => {
        hideError();
    }, 5000);
}

function hideError() {
    errorMessage.classList.remove('active');
}

function hideResult() {
    resultSection.classList.remove('active');
}

function formatDate(dateInput) {
    // Håndter både Date objekter og ISO strings
    const date = dateInput instanceof Date ? dateInput : new Date(dateInput);

    // Check om dato er valid
    if (isNaN(date.getTime())) {
        return 'Ukendt dato';
    }

    const day = String(date.getDate()).padStart(2, '0');
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const year = date.getFullYear();
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');

    return `${day}-${month}-${year} kl. ${hours}:${minutes}`;
}

function formatAnalysisPreview(analysis) {
    if (!analysis) return 'Ingen analyse';

    // Show first 150 characters + "..."
    const preview = analysis.substring(0, 150);
    return analysis.length > 150 ? preview + '...' : preview;
}

// ===== KEYBOARD SHORTCUTS =====
document.addEventListener('keydown', (e) => {
    // Escape key to close history
    if (e.key === 'Escape' && historySection.classList.contains('active')) {
        hideHistory();
    }
});

// ===== ERROR HANDLING =====
window.addEventListener('error', (event) => {
    console.error('Global fejl:', event.error);
});

// ===== CONSOLE INFO =====
console.log('%c🏃 SportResponse Frontend loaded ✅', 'color: #667eea; font-size: 16px; font-weight: bold;');
console.log('%cAPI Base URL:', 'font-weight: bold;', API_BASE_URL);