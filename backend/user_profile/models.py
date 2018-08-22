from django.db import models
from django.conf import settings
from django.contrib.auth import get_user_model
from django.db.models.signals import post_save
from django.dispatch import receiver


class UserProfile(models.Model):
    """Model For Extending Default Django User Model"""

    user = models.ForeignKey(
        settings.AUTH_USER_MODEL, on_delete=models.CASCADE, related_name='profile')
    room_number = models.IntegerField(default=0)

    def __str__(self):
        return f'{self.user.username} - Profile'


@receiver(post_save, sender=get_user_model())
def create_profile(sender, instance, created, *args, **kwargs):
    """Automatically Create A User Profile When A New User IS Registered"""

    if created:
        user_profile = UserProfile(user=instance)
        user_profile.save()
