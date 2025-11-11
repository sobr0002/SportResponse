import { getAllAnalyses} from "../api.js";

export async function renderAnalysesListView() {

    const container = document.getElementById("app");
    const analysesRows = await getAllAnalyses();

    if (!rows || rows.length === 0) {
        container.innerHTML = `<p>Ingen løbeanalyser fundet</p>`;

    }

    // Bygger HTML-tabellen som en String

    const tableHTML = `
       <section class="list-view">
       <h2>Alle løbeanalyser</h2>
       
       <table class="result-table">
       <thead>
       <tr>
       <th>Distance (km)</th>
       <th>Tid (min)</th>
       <th>Analyse</th>
       <th>Forslag</th>
       <th>Oprettelsesdato</th>
       </tr>
      </thead>
    <tbody>
    ${analysesRows
        .map(row => `
        <tr> 
        <td>${row.distance}</td>
        <td>${row.timeInMinutes}</td>
        <td>${row.analysis}</td>
        <td>${row.suggestions}</td>
        <td>${row.createdAt}</td></tr>`
    )
    .join("")}
        </tbody>
      </table>
      
      <button data-view="home" class="back-btn">Gå tilbage</button>
    </section>
`
    
    
    
    
}