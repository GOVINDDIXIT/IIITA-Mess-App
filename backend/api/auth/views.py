from django.contrib.auth import get_user_model
from rest_framework import generics
from . import serializers

User = get_user_model()


class UserProfileAPIView(generics.CreateAPIView):
    """API Endpoint To Create Profile"""

    queryset = User.objects.all()
    serializer_class = serializers.UserProfileSerializer
