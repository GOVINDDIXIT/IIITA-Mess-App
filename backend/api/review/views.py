from django.contrib.auth import get_user_model

from rest_framework import status
from rest_framework.decorators import api_view
from rest_framework.response import Response
from rest_framework import generics

from . import serializers
from review.models import Review

User = get_user_model()


class ReviewCreateView(generics.CreateAPIView):
    queryset = Review.objects.all()
    serializer_class = serializers.ReviewSerializer

    def create(self, request, *args, **kwargs):
        user_id = User.objects.get(username=request.data.get('username')).pk
        data = request.data.get('reviews')
        for review in data:
            review['user'] = user_id
        serializer = self.get_serializer(data=data, many=True)
        serializer.is_valid(raise_exception=True)
        self.perform_create(serializer)
        headers = self.get_success_headers(serializer.data)
        return Response(serializer.data, status=status.HTTP_201_CREATED, headers=headers)
