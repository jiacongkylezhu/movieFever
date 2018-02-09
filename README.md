# Movie Fever

## What is Movie Fever?

Movie Fever is an application allowing users to discover movies
  - Sort movies by three categories (Top rated, popular, and upcoming) 
  - Get the latest movie scores and reviews from critics
  - Watch trailers
  - Add movies to Favorites
 
![moviefever](https://user-images.githubusercontent.com/18712858/35963849-ee9cdb10-0c6a-11e8-8f4c-f403538a85c0.gif)


## Details
This project is used RecyclerView to load movie results in JSON format via OKHttp from an open source database The Movie DB(TMDB) It captures the Movie IDs, titles, and images and displays on the main activity. Handling orientation changes by using Loaders. Since the complex UI in MovieDetailActivity, the project is used Constrain Layout to handle the elements, used YouTube Player Fragment to play the trailer, and used a static fragment which contains a RecyclerView to display a list of reviews.   


## Libaraies
  
  - Picasso
  
  - OkHttp
  

## Installation
1. After clone or down the project, create a new bundle file

![mf1](https://user-images.githubusercontent.com/18712858/36053394-6719b13a-0da6-11e8-847b-97563bb07900.png)

2. Name it "keystore.properties" (only type "keystore" in the field)

![mf2](https://user-images.githubusercontent.com/18712858/36053110-26bb61de-0da5-11e8-9300-97d006063b88.png)

3. Copy and Paste the following into the keystore.propertie and insert your API keys int to the quotation marks:
```
YOUTUBE_KEY = "";
MOVIE_KEY = "";
```
![mf3](https://user-images.githubusercontent.com/18712858/36053111-26e9d852-0da5-11e8-99f8-27555a3c47f3.png)


## API Reference
Register TMDB and YouTube Player for API keys in the following:

TMDB:
API key can be found in "Account -> Settings -> API -> API key" after account registation.
https://www.themoviedb.org/documentation/api

YouTube: 
https://developers.google.com/youtube/android/player/register


## Authors
  * Jiacong (Kyle) Zhu
