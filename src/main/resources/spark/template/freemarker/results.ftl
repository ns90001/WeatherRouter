<#assign content>

<h1> RESULTS </h1>
<div>
    <a href="/stars" class="go-back"> Go back </a>
    <#if error??>
        <p>${error}</p>
    <#else>
        <table class="table table-striped">
            <thead>
            <tr>
                <th>ID</th>
                <th>Name</th>
                <th>X</th>
                <th>Y</th>
                <th>Z</th>
            </tr>
            </thead>
            <tbody>
            <#list stars as star>
                <tr>
                    <td>${star.id}</td>
                    <td>${star.name}</td>
                    <td>${star.x}</td>
                    <td>${star.y}</td>
                    <td>${star.z}</td>
                </tr>
            </#list>
            </tbody>
        </table>
    </#if>
</div>
</#assign>
<#include "main.ftl">