<!DOCTYPE html>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html lang="en">

<head>

  <meta charset="utf-8">

  <script type='text/javascript' src='https://code.jquery.com/jquery-2.1.4.js'></script>

  <script type='text/javascript' src='http://ajax.aspnetcdn.com/ajax/knockout/knockout-3.1.0.js'></script>
  
  <!-- Latest compiled and minified CSS -->
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.css">

  <!-- Optional theme -->
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap-theme.css">

  <!-- Latest compiled and minified JavaScript -->
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.js"></script>
  
  <script type='text/javascript' src='/static/js/ProjectViewModel.js'></script>
  
  <title><spring:message code="application.title" /></title>
</head>

<body>

  <div class="container">
    <form class="form-horizontal" role="form">
      <h2>${message}</h2>
      <div class="form-group">
        <label class="control-label col-sm-2" for="prjName">Project name:</label>
        <div class="col-sm-10">          
          <input type="input" class="form-control" id="prjName" placeholder="Enter project name" data-bind="value: prjName">
        </div>
      </div>
      <div class="form-group">        
        <div class="col-sm-offset-2 col-sm-10">
          <button class="btn btn-default" data-bind="click: query">Query</button>
        </div>
      </div>
      
      <div class="form-group">
        <div class="col-sm-offset-2 col-sm-10">
          <div class="col-sm-10">
            <table class="table table-striped form-group">
              <thead>
                <tr>
                  <th>Project ID</th>
                  <th>Project Name</th>
                  <th>Finishing Date</th>
                </tr>
              </thead>
              <tbody data-bind="foreach: projects">
                <tr>
                    <td><a data-bind="attr: {title: id, href: '/detail/' + id}, text: id"></a></td>
                    <td data-bind="text: name"></td>
                    <td data-bind="text: finishingDate"></td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </form>
  </div>   
  
</body>

</html>