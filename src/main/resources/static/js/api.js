const baseURL = "http://localhost:8080/api/runninganalyses"


export async function getAllAnalyses() {
    const list = await fetch(`${baseURL}/list`)
    return list.json();
}