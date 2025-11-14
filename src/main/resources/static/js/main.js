// main.js

const API_URL = 'http://localhost:8080/api/running';

// --- DOM ELEMENTER ---
const analyzeForm = document.getElementById('analyzeForm');
const distanceInput = document.getElementById('distance');
const timeInput = document.getElementById('time');
const btnText = document.getElementById('btnText');
const spinner = document.getElementById('spinner');
const errorMessage = document.getElementById('errorMessage');

// KNAPPER
const showHistoryBtn = document.getElementById('showHistoryBtn');
const showRecentBtn = document.getElementById('showRecentBtn');

// SEKTIONER
const resultSection = document.getElementById('resultSection');
const historySection = document.getElementById('historySection');
const recentSection = document.getElementById('recentSection');

// SELVE INDHOLDET
const analysisContent = document.getElementById('analysisContent');
const suggestionsContent = document.getElementById('suggestionsContent');
const historyRuns = document.getElementById('historyRuns');
const recentRuns = document.getElementById('recentRuns');

// --- EVENT LISTENERS ---
analyzeForm.addEventListener('submit', handleAnalyze);
showHistoryBtn.addEventListener('click', showHistory);
showRecentBtn.addEventListener('click', showRecent);

// --- HOVEDFUNKTIONER ---

/**
 * Håndterer formular submit - analyserer løb via AI
 */
async function handleAnalyze(event) {
    event.preventDefault();

    // Skjul tidligere resultater og fejl
    hideAllSections();
    hideError();

    // Vis loading state
    showLoading(true);

    // Hent input værdier
    const distance = parseFloat(distanceInput.value);
    const timeInMinutes = parseInt(timeInput.value);

    // Valider input
    if (!validateInput(distance, timeInMinutes)) {
        showLoading(false);
        return;
    }

    try {
        // Send request til backend
        const response = await fetch(`${API_URL}/analyze`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                distance: distance,
                timeInMinutes: timeInMinutes
            })
        });

        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }

        const data = await response.json();

        // Vis resultatet
        displayAnalysis(data);

        // Ryd formularen
        analyzeForm.reset();

    } catch (error) {
        console.error('Fejl ved analyse:', error);
        showError('Kunne ikke analysere løb. Prøv igen senere.');
    } finally {
        showLoading(false);
    }
}

/**
 * Viser alle løb (historik)
 */
async function showHistory() {
    hideAllSections();
    hideError();

    historySection.style.display = 'block';
    historyRuns.innerHTML = '<p class="loading">Henter historik...</p>';

    try {
        const response = await fetch(`${API_URL}/list`);

        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }

        const runs = await response.json();

        if (runs.length === 0) {
            historyRuns.innerHTML = '<p class="loading">Ingen løb endnu. Start med at analysere dit første løb!</p>';
            return;
        }

        displayRuns(runs, historyRuns);

    } catch (error) {
        console.error('Fejl ved hentning af historik:', error);
        historyRuns.innerHTML = '<p class="loading" style="color: #991B1B;">Kunne ikke hente historik.</p>';
    }
}

/**
 * Viser de seneste 10 løb
 */
async function showRecent() {
    hideAllSections();
    hideError();

    recentSection.style.display = 'block';
    recentRuns.innerHTML = '<p class="loading">Henter seneste løb...</p>';

    try {
        const response = await fetch(`${API_URL}/recent`);

        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }

        const runs = await response.json();

        if (runs.length === 0) {
            recentRuns.innerHTML = '<p class="loading">Ingen løb endnu. Start med at analysere dit første løb!</p>';
            return;
        }

        displayRuns(runs, recentRuns);

    } catch (error) {
        console.error('Fejl ved hentning af seneste løb:', error);
        recentRuns.innerHTML = '<p class="loading" style="color: #991B1B;">Kunne ikke hente seneste løb.</p>';
    }
}

/**
 * Sletter et løb
 */
async function deleteRun(id) {
    if (!confirm('Er du sikker på at du vil slette dette løb?')) {
        return;
    }

    try {
        const response = await fetch(`${API_URL}/${id}`, {
            method: 'DELETE'
        });

        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }

        // Opdater den aktuelle visning
        const historyVisible = historySection.style.display === 'block';
        const recentVisible = recentSection.style.display === 'block';

        if (historyVisible) {
            await showHistory();
        } else if (recentVisible) {
            await showRecent();
        }

    } catch (error) {
        console.error('Fejl ved sletning:', error);
        showError('Kunne ikke slette løb. Prøv igen.');
    }
}

// ===== DISPLAY FUNCTIONS =====

/**
 * Viser analyse resultat fra AI
 */
function displayAnalysis(data) {
    // Vis result section
    resultSection.style.display = 'block';

    // Vis analyse (pace, speed, estimater)
    analysisContent.innerHTML = formatAnalysis(data.analysis);

    // Vis træningsforslag
    suggestionsContent.innerHTML = formatSuggestions(data.suggestions);

    // Scroll til resultatet
    resultSection.scrollIntoView({ behavior: 'smooth', block: 'nearest' });
}

/**
 * Formaterer analyse tekst til pæn visning
 */
function formatAnalysis(analysisText) {
    if (!analysisText) return '<p>Ingen analyse tilgængelig.</p>';

    // Split på linjeskift og lav pæn formatering
    const lines = analysisText.split('\n').filter(line => line.trim() !== '');

    return lines.map(line => {
        // Fremhæv pace og speed
        if (line.includes('min/km') || line.includes('km/t')) {
            return `<p><strong>${line}</strong></p>`;
        }
        // Fremhæv halvmaraton og marathon
        if (line.toLowerCase().includes('marathon')) {
            return `<p><strong>${line}</strong></p>`;
        }
        return `<p>${line}</p>`;
    }).join('');
}

/**
 * Formaterer træningsforslag til pæn visning
 */


function formatSuggestions(suggestionsText) { // Får String fra backend AI response
    if (!suggestionsText) return '<p>Ingen forslag tilgængelige.</p>';

    // Forslagene splittes til et array. Èt forslag = ét element, som er separeret af 2 linjeskift
    const suggestions = suggestionsText.split('\n\n').filter(s => s.trim() !== '');

    // Hvert forslag konverteres til type og description, når der er kolon
    return suggestions.map(suggestion => {
        const colonIndex = suggestion.indexOf(':');

        let type = suggestion;
        let description = '';

        // .slice() deler forslaget op i type og description, hvis der er kolon
        if (colonIndex !== -1) {
            type = suggestion.slice(0, colonIndex).trim(); // Position før kolon
            description = suggestion.slice(colonIndex + 1).trim(); // Position efter kolon
        }

        return `
            <div class="suggestion-card">
                <h4>💡 ${type}</h4>
                <p>${description}</p>
            </div>
        `;
    }).join(''); // Samler til én String
}

/**
 * Viser løb i et grid (historik eller recent)
 */
function displayRuns(runs, container) {
    if (!runs || runs.length === 0) {
        container.innerHTML = '<p class="loading">Ingen løb fundet.</p>';
        return;
    }

    container.innerHTML = runs.map(run => createRunCard(run)).join('');

    // Tilføj event listeners til delete knapper
    document.querySelectorAll('.btn-delete').forEach(btn => {
        btn.addEventListener('click', (e) => {
            const runId = e.target.dataset.id;
            deleteRun(runId);
        });
    });
}

/**
 * Opretter HTML for et enkelt løb card
 */
function createRunCard(run) {
    const date = formatDate(run.createdAt);

    return `
        <div class="run-card">
            <div class="run-info">
                <h4>🏃 ${run.distance} km på ${run.timeInMinutes} minutter</h4>
                <p class="run-meta">
                    Pace: ${run.paceFormatted} | ${date}
                </p>
            </div>
            <button class="btn-delete" data-id="${run.id}">Slet</button>
        </div>
    `;
}

// --- Hjælpemetoder ---

/**
 * Validerer input fra formularen
 */
function validateInput(distance, time) {
    if (isNaN(distance) || distance <= 0) {
        showError('Distance skal være et positivt tal.');
        return false;
    }

    if (isNaN(time) || time <= 0) {
        showError('Tid skal være et positivt tal.');
        return false;
    }

    if (distance > 100) {
        showError('Distance kan ikke være over 100 km.');
        return false;
    }

    if (time > 600) {
        showError('Tid kan ikke være over 600 minutter (10 timer).');
        return false;
    }

    return true;
}



/**
 * Formaterer dato til dansk format
 */
function formatDate(dateString) {
    const date = new Date(dateString);
    const day = date.getDate().toString().padStart(2, '0'); // padstart() sikrer at der altid er 2 cifre, f.eks. 01 til 09
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const year = date.getFullYear();
    const hours = date.getHours().toString().padStart(2, '0');
    const minutes = date.getMinutes().toString().padStart(2, '0');

    return `${day}-${month}-${year} kl. ${hours}:${minutes}`; // js template literal / string
}

/**
 * Viser/skjuler loading spinner
 */
function showLoading(isLoading) {
    if (isLoading) {
        btnText.textContent = 'Analyserer...';
        spinner.style.display = 'inline-block'; // viser elementet i samme linje som teksten og fylder hele boksen
        analyzeForm.querySelector('button[type="submit"]').disabled = true;
    } else {
        btnText.textContent = 'Analyser løb';
        spinner.style.display = 'none';
        analyzeForm.querySelector('button[type="submit"]').disabled = false;
    }
}

/**
 * Viser fejlbesked
 */
function showError(message) {
    errorMessage.textContent = message;
    errorMessage.style.display = 'block'; // viser elementet som et block element, fylder hele linjen

    // Scroll til fejlbeskeden
    errorMessage.scrollIntoView({ behavior: 'smooth', block: 'nearest' });
}

/**
 * Skjuler fejlbesked
 */
function hideError() {
    errorMessage.style.display = 'none';
}

/**
 * Skjuler alle sektioner (result, history, recent)
 */
function hideAllSections() {
    resultSection.style.display = 'none';
    historySection.style.display = 'none';
    recentSection.style.display = 'none';
}

// Når siden loader, vis ingenting - vent på bruger interaction
document.addEventListener('DOMContentLoaded', () => {
    console.log('Running Coach initialiseret! 🏃');
    hideAllSections();
});