/**
 * Created by Sebastian on 2016-05-08.
 */
postControllers.controller('AdminController',
    ['AdminService', 'PostService', '$http', '$route', '$scope', '$routeParams', function (AdminService, PostService, $http, $route, $scope, $routeParams) {
        $scope.clientId = $routeParams.id;
        $scope.searchText = '';

        var init = function () {
            console.log("Initializing front page");
            PostService.getPosts(function (posts) {
                $scope.postsList = posts;
                console.log($scope.postsList);
            })
        }
        init();

        $scope.deletePost = function (uUID) {
            PostService.deletePost({id: uUID}, function (info) {
                console.log(info);
            })
            $route.reload();
        }
        $scope.search = function (row) {
            return (angular.lowercase(row.title).indexOf(angular.lowercase($scope.searchText)) !== -1 ||
            angular.lowercase(row.keywords).indexOf(angular.lowercase($scope.searchText)) !== -1 ||
            $scope.searchText == ' ');
        };

    }]);

