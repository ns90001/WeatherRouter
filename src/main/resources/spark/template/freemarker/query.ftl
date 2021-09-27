<#assign content>

<h1> Stars Query </h1>

<div id="content" role="main">
  <p class="query-title"> Search for neighbors:</p>
  <div class="query-box">
    <form method="GET" action="/neighbors">
      <div class="target-type">
        <span>Target type</span> <br>
        <input type="radio" id="neighbors-name" name="target" value="name" checked>
        <label for="neighbors-name">Name</label><br>
        <input type="radio" id="neighbors-coords" name="target" value="coords">
        <label for="neighbors-coords">Coords</label><br>
      </div>
      <div class="search-type">
        <span>Search type</span> <br>
        <input type="radio" id="neighbors-naive" name="search" value="naive" checked>
        <label for="neighbors-naive">Naive</label><br>
        <input type="radio" id="neighbors-kd" name="search" value="kd">
        <label for="neighbors-kd">KD-Tree</label><br>
      </div>
      <div class="input-line">
        <label for="num-neighbors">Number of neighbors:</label><br>
        <input type="text" id="num-neighbors" class="sm-text" name="num-neighbors"><br>
      </div>
      <div id="n-target-name">
        <label for="name-neighbors">Name of target:</label><br>
        <input type="text" id="name-neighbors" name="name-neighbors"><br>
      </div>
      <div id="n-target-coords">
        <span>Target coordinates:</span><br>
        <div class="coords">
          <div class="input-line">
            <label for="x-neighbors">x:</label><br>
            <input type="text" id="x-neighbors" class="sm-text" name="x-neighbors"><br>
          </div>
          <div class="input-line">
            <label for="y-neighbors">y:</label><br>
            <input type="text" id="y-neighbors" class="sm-text" name="y-neighbors"><br>
          </div>
          <div class="input-line">
            <label for="z-neighbors">z:</label><br>
            <input type="text" id="z-neighbors" class="sm-text" name="z-neighbors"><br>
          </div>
        </div>
      </div>
      <input type="submit" class="submit" value="Find nearest neighbors">
    </form>
  </div>

  <p class="query-title"> Search by radius: </p>
  <div class="query-box">
    <form method="GET" action="/radius">
      <div class="target-type">
        <span>Target type</span> <br>
        <input type="radio" id="radius-name" name="target" value="name" checked>
        <label for="radius-name">Name</label><br>
        <input type="radio" id="radius-coords" name="target" value="coords">
        <label for="radius-coords">Coords</label><br>
      </div>
      <div class="search-type">
        <span>Search type</span> <br>
        <input type="radio" id="radius-naive" name="search" value="naive" checked>
        <label for="radius-naive">Naive</label><br>
        <input type="radio" id="radius-kd" name="search" value="kd">
        <label for="radius-kd">KD-Tree</label><br>
      </div>
      <div class="input-line">
        <label for="rad">Radius:</label><br>
        <input type="text" id="rad" class="sm-text" name="rad"><br>
      </div>
      <div id="r-target-name">
        <label for="name-radius">Name of target:</label><br>
        <input type="text" id="name-radius" name="name-radius"><br>
      </div>
      <div id="r-target-coords">
        <span>Target coordinates:</span><br>
        <div class="coords">
          <div class="input-line">
            <label for="x-radius">x:</label><br>
            <input type="text" id="x-radius" class="sm-text" name="x-radius"><br>
          </div>
          <div class="input-line">
            <label for="y-radius">y:</label><br>
            <input type="text" id="y-radius" class="sm-text" name="y-radius"><br>
          </div>
          <div class="input-line">
            <label for="z-radius">z:</label><br>
            <input type="text" id="z-radius" class="sm-text" name="z-radius"><br>
          </div>
        </div>
      </div>
      <input type="submit" class="submit" value="Find stars within radius">
    </form>
  </div>
</div>

</#assign>
<#include "main.ftl">