# Introdução

Este é um projeto desenvolvido para praticar os conhecimentos de Redis / Docker do curso realizado no Alura. A ideia é baseada no link 
http://www.agoragames.com/blog/2011/01/01/creating-high-score-tables-leaderboards-using-redis/

# Build

```
$ ./mvnw package
$ docker-compose build
$ docker-compose up -d
```


# Registrar Pontuação

```
$ curl -v -H "Content-Type: application/json" -X POST -d '{"userId": 53185,"score": 50, "description": "Conclusão do Curso Redis I: Armazenando chaves e valores!","memberData": "{\"id\":\"40505612\",\"thumbnailUrl\":\"https://secure.gravatar.com/avatar/8aec855e87715450db1bccac40c48503\",\"name\":\"Lucas Oliveira\",\"displayName\":\"luksrn\"}"}' http://localhost:8080/rank
$ curl -v -H "Content-Type: application/json" -X POST -d '{"userId": 53185,"score": 200, "description": "Curso Redis II: Estruturas e recursos na sua base NoSQL!","memberData": "{\"id\":\"647532\",\"thumbnailUrl\":\"https://secure.gravatar.com/avatar/8aec855e87715450db1bccac40c48503\",\"name\":\"Oliveira\",\"displayName\":\"oliveira\"}"}' http://localhost:8080/rank
$ curl -v -H "Content-Type: application/json" -X POST -d '{"userId": 7569143,"score": 100, "description": "Curso Docker: Criando containers sem dor de cabeça","memberData": "{\"id\":\"75484345\",\"thumbnailUrl\":\"https://secure.gravatar.com/avatar/b0e80840a42d52529678137163170afd\",\"name\":\"Lucas Farias de Oliveira\",\"displayName\":\"lucasfdo\"}"}' http://localhost:8080/rank
$ curl -v -H "Content-Type: application/json" -X POST -d '{"userId": 7569143,"score": 100, "description": "Curso Docker: Criando containers sem dor de cabeça","memberData": "{\"id\":\"75484345\",\"thumbnailUrl\":\"https://secure.gravatar.com/avatar/b0e80840a42d52529678137163170afd\",\"name\":\"Lucas Farias de Oliveira\",\"displayName\":\"lucasfdo\"}"}' http://localhost:8080/rank
```

# Consultar Ranking

$ curl -X GET http://localhost:8080/rank | json_pp
```
{
   "_embedded" : {
      "membersRanked" : [
         {
            "key" : "53185",
            "rank" : 0,
            "userData" : {
               "thumbnailUrl" : "https://secure.gravatar.com/avatar/8aec855e87715450db1bccac40c48503",
               "id" : "647532",
               "name" : "Oliveira",
               "displayName" : "oliveira"
            },
            "score" : 250,
            "_links" : {
               "arround-me" : {
                  "templated" : true,
                  "href" : "http://localhost:8080/rank/member/53185/around-me{?pageSize}"
               },
               "scores" : {
                  "href" : "http://localhost:8080/rank/member/53185/scores"
               },
               "self" : {
                  "href" : "http://localhost:8080/rank/member/53185"
               },
               "latest-activities" : {
                  "href" : "http://localhost:8080/rank/member/53185/actions"
               }
            }
         },
         {
            "_links" : {
               "arround-me" : {
                  "templated" : true,
                  "href" : "http://localhost:8080/rank/member/7569143/around-me{?pageSize}"
               },
               "latest-activities" : {
                  "href" : "http://localhost:8080/rank/member/7569143/actions"
               },
               "self" : {
                  "href" : "http://localhost:8080/rank/member/7569143"
               },
               "scores" : {
                  "href" : "http://localhost:8080/rank/member/7569143/scores"
               }
            },
            "score" : 200,
            "key" : "7569143",
            "rank" : 1,
            "userData" : {
               "displayName" : "lucasfdo",
               "thumbnailUrl" : "https://secure.gravatar.com/avatar/b0e80840a42d52529678137163170afd",
               "id" : "75484345",
               "name" : "Lucas Farias de Oliveira"
            }
         }
      ]
   }
}
```
# Consulta das atividades

$ curl -X GET http://localhost:8080/rank/member/53185/actions | json_pp
```
{
   "activities" : [
      {
         "timestamp" : "2017-12-17T02:16:43.214",
         "description" : "Curso Redis II: Estruturas e recursos na sua base NoSQL!",
         "score" : 200
      },
      {
         "score" : 50,
         "description" : "Conclusão do Curso Redis I: Armazenando chaves e valores!",
         "timestamp" : "2017-12-17T02:16:35.37"
      }
   ],
   "_links" : {
      "scores" : {
         "href" : "http://localhost:8080/rank/member/53185/scores"
      },
      "rank" : {
         "href" : "http://localhost:8080/rank/member/53185"
      },
      "self" : {
         "href" : "http://localhost:8080/rank/member/53185/actions"
      }
   }
}
```
# Consulta das Pontuações do último mês

$ curl -X GET  http://localhost:8080/rank/member/53185/scores | json_pp
```
{
   "scores" : [
      {
         "day" : "2017-11-17",
         "scores" : 0
      },
      {
         "scores" : 0,
         "day" : "2017-11-18"
      },
      {
         "day" : "2017-11-19",
         "scores" : 0
      },
      ,...
      {
         "day" : "2017-12-16",
         "scores" : 0
      },
      {
         "day" : "2017-12-17",
         "scores" : 250
      }
   ],
   "_links" : {
      "self" : {
         "href" : "http://localhost:8080/rank/member/53185/scores"
      },
      "arround-me" : {
         "href" : "http://localhost:8080/rank/member/53185/around-me{?pageSize}",
         "templated" : true
      },
      "latest-activities" : {
         "href" : "http://localhost:8080/rank/member/53185/actions"
      },
      "rank" : {
         "href" : "http://localhost:8080/rank/member/53185"
      }
   }
}
```


