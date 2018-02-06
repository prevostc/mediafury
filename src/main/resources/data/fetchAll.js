
const apiKey = "XXXXXX";

(function(apiKey) {
    return Promise.all(
        Array.from(new Array(40))
            .map((_, i) => i+1)
            .map(i => `http://www.omdbapi.com/?s=harry&page=${i}&apikey=${apiKey}`)
            .map(url => fetch(url))
            .map(p => p
                .then(r => r.json())
                .then(j => j["Search"])
            )
    )
        .then(r => Promise.all(
            r.reduce((agg, e) => agg.concat(e), [])
                .map(e => e.imdbID)
                .map(id => `http://www.omdbapi.com/?i=${id}&plot=full&apikey=${apiKey}`)
                .map(url => fetch(url))
                .map(p => p.then(r => r.json()))
        ))
})(apiKey)
