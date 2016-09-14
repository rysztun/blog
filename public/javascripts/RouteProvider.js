var mainApp = angular.module("mainApp", ['ngRoute', 'PostControllers']);

mainApp.config(['$routeProvider', '$locationProvider', function ($routeProvider, $locationProvider) {
    $routeProvider.when('/post/add', {
        templateUrl: '/assets/templates/addPost.html',
        controller: 'PostAddController'
    }).when('/post/:id', {
        templateUrl: '/assets/templates/postDetails.html',
        controller: 'PostDetailsController'
    }).when('/post/update/:id', {
        templateUrl: '/assets/templates/addPost.html',
        controller: 'PostUpdateController'
    }).when('/admin', {
        templateUrl: '/assets/templates/adminPage.html',
        controller: 'AdminController'
    }).otherwise({
        templateUrl: '/assets/templates/posts.html',
        controller: 'PostController'
    });

    //$locationProvider.html5Mode({
    //        enabled: true,
    //        requireBase: false
    //    });
}]);