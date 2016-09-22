postControllers.controller('PostDetailsController',
    ['PostService', 'CommentService', '$route', '$scope', '$routeParams', function (PostService, CommentService, $route, $scope, $routeParams) {
        $scope.id = $routeParams.id;
        $scope.newComment = {};
        $scope.newComment.author = '';
        $scope.newComment.comment = '';
        $scope.newComment.uUID = '';
        $scope.errorMessage = '';


        PostService.findPost({id: $scope.id}, function (post) {
            console.log(post);
            $scope.post = post;
        });

        CommentService.getComments({id: $scope.id}, function (comments) {
            console.log(comments[0]);
            $scope.commentsList = comments;
        });

        $scope.addComment = function () {
            $scope.newComment.uUID = $scope.post.uUID;
            console.log($scope.newComment);
            CommentService.addComment($scope.newComment, function (info) {
                console.log('comment added');
                $route.reload();
            });
        }
    }]);