<!DOCTYPE html>
<html>
<head>
    <title>Search Compare</title>

    <style>
        body{
            font-family: Arial;
            margin:0;
        }
        .top{
            padding:20px;
            background:#222;
            text-align:center;
        }
        input{
            width:60%;
            padding:15px;
            font-size:20px;
        }
        button{
            padding:15px 30px;
            font-size:18px;
            cursor:pointer;
        }
        .container{
            display:flex;
            height:90vh;
        }
        .panel{
            flex:1;
            border:1px solid #ccc;
            overflow:auto;
            padding:20px;
        }
        h2{
            margin-top:0;
        }
        .result{
            background:#f5f5f5;
            margin:5px;
            padding:10px;
        }
        .time{
            font-weight:bold;
            color:green;
        }
    </style>
</head>
<body>

<div class="top">
    <input id="query" placeholder="Type search here..." />
    <button onclick="search()">SEARCH</button>
</div>

<div class="container">
    <div class="panel">
        <h2>Postgres</h2>
        <div id="pgTime" class="time"></div>
        <div id="pgResult"></div>
    </div>

    <div class="panel">
        <h2>Elasticsearch</h2>
        <div id="esTime" class="time"></div>
        <div id="esResult"></div>
    </div>
</div>
<script>
    // async function search(){
    //     const q = document.getElementById("query").value;
    //     const response = await fetch("/api/compare/products?query=" + q);
    //     const data = await response.json();
    //     // TIME
    //     document.getElementById("pgTime").innerText =  "Time: " + data.postgres_time_ms + " ms";
    //     document.getElementById("esTime").innerText =  "Time: " + data.elastic_time_ms + " ms";
    //     // RESULTS
    //     render("pgResult", data.postgres_data);
    //     render("esResult", data.elastic_data);
    // }
    function search() {
        const q = document.getElementById("query").value;

        // Reset UI
        document.getElementById("pgResult").innerHTML = "Searching...";
        document.getElementById("esResult").innerHTML = "Searching...";
        document.getElementById("pgTime").innerText = "";
        document.getElementById("esTime").innerText = "";
        // 1. Call Postgres (Parallel)
        fetch("/api/search/postgres?q=" + q)
            .then(response => response.json())
            .then(data => {
                document.getElementById("pgTime").innerText = "Time: " + data.time + " ms";
                render("pgResult", data.data);
            });
        // 2. Call Elasticsearch (Parallel)
        fetch("/api/search/elastic?q=" + q)
            .then(response => response.json())
            .then(data => {
                document.getElementById("esTime").innerText = "Time: " + data.time + " ms";
                render("esResult", data.data);
            });
    }

    function render(elementId, list){
        const el = document.getElementById(elementId);
        el.innerHTML = "";
        list.forEach(item => {
            const div = document.createElement("div");
            div.className = "result";
            div.innerText =
                "ID: " + item.id + " | " + item.title;
            el.appendChild(div);
        });
    }

</script>

</body>
</html>