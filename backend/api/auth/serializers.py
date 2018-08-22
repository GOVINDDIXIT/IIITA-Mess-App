from django.contrib.auth import get_user_model
from rest_framework import serializers
from user_profile.models import UserProfile

from user_profile.models import UserProfile

User = get_user_model()


class UserProfileSerializer(serializers.ModelSerializer):
    """Serializer For Creating User Profile"""

    room_number = serializers.IntegerField(
        source='profile.room_number', allow_null=True)

    class Meta:
        model = User
        fields = ['username', 'email', 'first_name',
                  'last_name', 'room_number']

    def create(self, validated_data):
        profile_data = validated_data.pop('profile', None)
        instance = User.objects.create(**validated_data)
        instance_profile = instance.profile.get()
        instance_profile.room_number = profile_data.get('room_number')
        instance_profile.save()
        return instance
