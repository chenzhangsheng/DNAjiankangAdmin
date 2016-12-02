'use strict';

/* Controllers */
  // signin controller
app.controller('SigninFormController', ['$scope', '$http', '$state', function($scope, $http, $state) {
    $scope.user = {};
    $scope.authError = null;
    $scope.login = function() {
      $scope.authError = null;
      // Try to login
   
      $http.post('api/login', {email: $scope.user.email, password: $scope.user.password})
      .then(function(response) {
        
       /*console.log($scope.user.email);*/
        console.log(response.data)
        if (response.data.user.email==$scope.user.email) {
           $state.go('app.dashboard-v1');
        }else{
         $scope.authError = 'Email or Password not right';
        }
      }, function(x) {
        $scope.authError = 'Server Error';
      });
    };
  }])
;