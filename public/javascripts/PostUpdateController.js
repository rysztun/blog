postControllers.controller('PostUpdateController',
    ['PostService', '$http', '$route', '$scope', '$routeParams', function (PostService, $http, $route, $scope, $routeParams) {

        $scope.pageTitle = 'Update your post';
        $scope.buttonAction = 'Update post';
        $scope.id = $routeParams.id;
        $scope.newPost = {};
        $scope.newPost.uUID = '';
        $scope.newPost.title = '';
        $scope.newPost.content = '';

        var init = function () {
            PostService.findPost({id: $scope.id}, function (post) {
                console.log(post);
                $scope.newPost = post;
            });
        };
        init();

        $scope.addPost = function () {
            console.log($scope.newPost)
            PostService.updatePost({id: $scope.id}, $scope.newPost, function (info) {
                console.log('post actualised');
                $route.reload();
            });
        }
    }]);