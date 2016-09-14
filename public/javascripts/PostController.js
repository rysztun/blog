var postControllers = angular.module('PostControllers', ['ngResource']);

postControllers.controller('PostController',
    ['PostService', '$scope', function (PostService, $scope) {
        'use strict';
        $scope.searchText = '';

        var init = function () {
            console.log("Initializing front page");
            PostService.getPosts(function (posts) {
                $scope.postsList = posts;
                console.log($scope.postsList);
            })
        };
        init();

        $scope.search = function (row) {
            return (angular.lowercase(row.title).indexOf(angular.lowercase($scope.searchText)) !== -1 ||
            angular.lowercase(row.keywords).indexOf(angular.lowercase($scope.searchText)) !== -1 ||
            $scope.searchText == ' ');
        };

    }]);