const http = require("http");
const fs = require("fs");
const socketio = require('socket.io');
const port = 8004
const parse = require('node-html-parser').parse;

var server = http.createServer((req, res) => {
	if(req.method != "POST") {	
		responseCode = 200;
		if (req.url.toLowerCase() != "/halloffame") {
			content = fs.readFileSync("./index.html");
			res.writeHead(responseCode, {
				"content-type": "text/html;charset=utf-8",
			});
			res.write(content);
			res.end();
		} else {
			content = fs.readFileSync("./halloffame.html");
			res.writeHead(responseCode, {
				"content-type": "text/html;charset=utf-8",
			});
			res.write(content);
			res.end();
		}
	} else {
		//console.log(req);
		if(req.url == "/sendNotification") {
			let body = "";
			req.on("data", chunk => {
				body += chunk.toString(); // convert Buffer to string
			});
			req.on("end", () => {
                let seperator = /\+/g
                console.log(body.split("=")[1].replace(seperator, " "));
				help(body.split("=")[1].replace(seperator, " "))
				res.end("sendNotification h");
			});
		}
		
		if(req.url == "/get") {
			
			let body = "";
			req.on("data", chunk => {
				body += chunk.toString(); // convert Buffer to string
			});
			req.on("end", () => {
                res.end("get h");
            });
		} else {
			//if statement here
		}
	}
})
.listen(port);
const io = socketio(server)
io.on("connection", function (socket) {
	console.log("Made socket connection");
	socket.on("fame", function (args) {
		try {
			let password = JSON.parse(args).password
			console.log(fs.readFileSync("password.txt").toString())
		if (password == fs.readFileSync("password.txt")) {
			let data = JSON.parse(args).message
			fs.readFile('halloffame.html', 'utf8', (err,html)=>{
	  		    if(err){
    				throw err;
			    }
				const root = parse(html);
   				const body = root.querySelector('#halloffame');
   				body.set_content('<p>' + data + '</p> <br>' + body);
				fs.writeFileSync('halloffame.html', root.toString(), "utf8" )
 			});
		}
		}
		catch { 
			console.log("lol someone tried to hack the hall of fame api lolllll")
		}
		
	  });
});

function help(data) {
	io.emit("sendnotif", data)
}
  