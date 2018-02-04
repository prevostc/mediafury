
const apiKey = "XXXXXX";

let allMovies = [];
Promise.all(
    Array.from(new Array(20))
    .map((_, i) => i+8)
    .map(i => `http://www.omdbapi.com/?s=batman&page=${i}&apikey=${apiKey}`)
    .map(url => fetch(url))
    .map(p => p
        .then(r => r.json())
        .then(j => j["Search"])
    )
)
.then(r => {
    allMovies = r.reduce((agg, e) => agg.concat(e), [])
        .map(e => e.imdbID)
        .map(id => `http://www.omdbapi.com/?i=${id}&plot=full&apikey=${apiKey}`)
        .map(url => fetch(url))
        .map(p => p.then(r => r.json()).then(m => allMovies.push(m)))
    console.log("movies", allMovies);
})
