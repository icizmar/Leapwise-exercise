# Leapwise - exercise
Implement a hot topic analysis for RSS feeds.

## Specification
Application exposes two HTTP endpoints:


### API Definition:

*HTTP method - POST*
```
/analyse/new
```


### API Input
This API endpoint takes at least two RSS URLs as a parameter (more are possible) in request body:

Example:
```
[
  "https://news.google.com/news?cf=all&hl=en&pz=1&ned=us&output=rss",
  "http://rssblog.whatisrss.com/feed/"
  ]
 ```
  
  
### API Response:
For each request executed against the API endpoint, it returns an unique identifier, which is the input for the second API endpoint.

Example:
```
cad0c33c-a4c6-4142-81d3-fb293e0932b6
```


### API Definition:

*HTTP method - GET*
```
/frequency/{id}
```


### API Input:
This API endpoint takes an id as input.

Example:
```
localhost:8080/frequency/cad0c33c-a4c6-4142-81d3-fb293e0932b6
```


### API Response:
Returns the JSON of three elements with the most matches with news title and the link to the whole news text.
```
[
    {
        "tagName": "tag_name",
        "feeds": [
            {
                "title": "Title - Title News",
                "link": "link.com"
            },...
            
```


### Installation and running the exercise with maven

Clone the repository:
````
git clone https://github.com/icizmar/Leapwise-exercise.git
````

Execute from application directory
```
mvn spring-boot:run
```
