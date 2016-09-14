postControllers.controller('PostAddController',
    ['PostService', '$route', '$scope', function (PostService, $route, $scope) {
        $scope.pageTitle = 'Add new post here!';
        $scope.buttonAction = 'Add post';
        $scope.newPost = {};
        $scope.newPost.uUID = '';
        $scope.newPost.title = '';
        $scope.newPost.content = '';
        $scope.newPost.date = {};

        $scope.addPost = function () {
            console.log($scope.newPost);
            $scope.newPost.date = new Date().getTime();
            PostService.createPost($scope.newPost, function (info) {
                console.log('added new post');
                $route.reload();
            });
        }

    }]);