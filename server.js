  require('dotenv').config()
const Discord = require('discord.js');
const client = new Discord.Client();
var fs = require('fs');
var request = require('request');
const app = require('express')();
const http = require("http")
app.get("/", (request, response) => {
    response.sendStatus(200)
});
app.get("/bestemmie", (request, response) => {
    response.sendFile('bestemmie.txt' , { root : __dirname});
});
app.listen(process.env.PORT);
setInterval(() => {
    http.get('http://' + process.env.PROJECT_DOMAIN + '.glitch.me/')
}, 60000);

client.on('ready', () => {
    var i = 0
    const presence = [
        "b!help",
        "Bestemmiate tanto!",
        "Developed by Rattly",
        client.users.size + " users",
        "Open-Source! | b!source"
    ]

    setInterval(() => {
        if (i === presence.length) i = 0;
        client.user.setActivity((presence[i]), {
            type: 'STREAMING',
            url: 'https://www.twitch.tv/sperial'
        });
        i++;
    }, 3500);
    client.user.setStatus("online");
    console.log(`Logged in as ${client.user.tag}!`);
});

client.on('message', msg => {
    if (msg.content === "b!help") {
        const embed = new Discord.RichEmbed()
            .setColor('RANDOM')
            .setTitle('Bestemmiometro')
            .setDescription('il bot delle bestemmie divine')

            .addField('Come funziona', 'Bestemmia e la tua bestemmia sarà contata. b!levels per i vari livelli')
            .setTimestamp()
            .setFooter('Creato da Rattly con tanto amore');

        msg.channel.send(embed)
    }
    if (msg.content === "b!levels") {
        const embed = new Discord.RichEmbed()
            .setColor('RANDOM')
            .setTitle('Bestemmiometro')
            .setDescription('il bot delle bestemmie divine')

            .addField('Livelli', 'livello 1: 15 bestemmie: Starter delle Bestemmie\nlivello 2: 45 bestemmie\nlivello 3: 105 bestemmie: Padre Pio\nlivello 4: 210 bestemmie\nlivello 5: 400 bestemmie: MatteoHS\nlivello 6: 600 bestemmie\nlivello 7: 1000 bestemmie: AntiCristo\nlivello 8: 1300 bestemmie\nlivello 9: 1650 bestemmie\nlivello 10: 2000 bestemmie: King delle Bestemmie\nDio dei Dii: 15000 bestemmie')
            .addField('Come livellare', "1. Bestemmiare molto.\n2. Il livellamento sarà automatico")
            .setTimestamp()
            .setFooter('Creato da Rattly con tanto amore');

        msg.channel.send(embed)
    }
    if (msg.content === "b!source") {
        const embed = new Discord.RichEmbed()
            .setColor('RANDOM')
            .setTitle('Bestemmiometro')
            .setDescription('il bot delle bestemmie divine')

            .addField('Source', 'https://glitch.com/~bestemmiometro')
            .setTimestamp()
            .setFooter('Creato da Rattly con tanto amore');

        msg.channel.send(embed)
    }
    if (msg.content === "b!check") {
      
        checkBestemmie(msg.author.id).then(function (body) {

            checkLivello(msg.author.id).then(function (body1) {
        var i;
        var count = 0;
        require('fs').createReadStream("bestemmie.txt")
            .on('data', function(chunk) {
                for (i = 0; i < chunk.length; ++i)
                    if (chunk[i] == 10) count++;
            })
            .on('end', function() {
                const embed = new Discord.RichEmbed()
                    .setColor('RANDOM')
                    .setTitle('Bestemmiometro')
                    .setDescription('il bot delle bestemmie divine')

                    .addField('Totale delle bestemmie pronunciate', body)
                    .addField('Livello', body1)
                    .addField('Bestemmie nel Database', count)
                    .setTimestamp()
                    .setFooter('Creato da Rattly con tanto amore');

                msg.channel.send(embed)
        })
              })
            });
    }
    const str = msg.content;
    var fs = require('fs'),
        path = require('path'),
        filePath = path.join(__dirname, 'bestemmie.txt');

    fs.readFile(filePath, {
        encoding: 'utf-8'
    }, function(err, data) {
        if (!err) {
            var arr = data.split("\n")
            for (let i = 0; i < arr.length; i++) {
                const elem = arr[i];
                if (str.toLowerCase().includes(elem)) {
                    addBestemmia(msg.author.id).then(function () {
                    checkBestemmie(msg.author.id).then(function (body) {
                      msg.channel.send("Ora hai "+body+" bestemmie")
                      addLivello(msg.author.id).then(function (ok) {
                        if(ok !== "nope") {
                          checkLivello(msg.author.id).then(function (body1){ 
                          msg.channel.send("Sei anche passato di livello! Livello: "+body1)
                            })
                        }
                      })
                    })
                  })
                }
            }
        } else {
            console.log(err);
        }
    });
});

client.login(process.env.TOKEN);

//addare le bestemmie
function addBestemmia(id) {
      return new Promise(function(resolve, reject) {
    var base = "bibbibubbubabba.altervista.org"
    var path = "/bestemmiometro/storage.php?secret=" + process.env.SECRET;
    var options = {
        host: base,
        port: 80,
        path: path + "&addid=" + id
    };

    http.get(options, function(res) {
        var body = '';
        res.on('data', function(chunk) {
            body += chunk;
        });
        res.on('end', function() {
          resolve()
        });
    }).on('error', function(e) {
        console.log("Got error: " + e.message);
    });
        })
}


function checkBestemmie(id) {
  return new Promise(function(resolve, reject) {
                        var base = "bibbibubbubabba.altervista.org"
                    var path = "/bestemmiometro/storage.php?secret=" + process.env.SECRET;
                    var options = {
                        host: base,
                        port: 80,
                        path: path + "&bestemmie=" + id
                    };

                    http.get(options, function(res) {
                        var body = '';
                        res.on('data', function(chunk) {
                            body += chunk;
                        });
                        res.on('end', function() {
                           resolve(body)
                        });
                    }).on('error', function(e) {
                        console.log("Got error: " + e.message);
                      reject(e.message)
                    });
  })
  
}

function addLivello(id) {
  return new Promise(function(resolve, reject) {
    request("http://bibbibubbubabba.altervista.org/bestemmiometro/storage.php?secret=" + process.env.SECRET + "&bestemmie=" + id, function(error, response, body) {
      request("http://bibbibubbubabba.altervista.org/bestemmiometro/storage.php?secret=" + process.env.SECRET + "&addliv=" + id + "&num=" + body, function(error, response, body1) {
        if(body1 === "nope") {
          resolve("nope")
        } else {
          resolve("ok")
        }
      });
    });
  });
}

function checkLivello(id) {
  return new Promise(function(resolve, reject) {
    request("http://bibbibubbubabba.altervista.org/bestemmiometro/storage.php?secret=" + process.env.SECRET + "&livelli=" + id, function(error, response, body1) {
      resolve(body1)
    })
  })
}

function modBestemmie(id, num) {
  return new Promise(function(resolve, reject) {
    request("http://bibbibubbubabba.altervista.org/bestemmiometro/storage.php?secret=" + process.env.SECRET + "&modbest=" + id + "&num="+ num, function(error, response, body1) {
      resolve()
    })
  })
}
