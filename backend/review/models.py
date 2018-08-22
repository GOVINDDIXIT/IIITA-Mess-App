from django.db import models
from django.conf import settings


class Review(models.Model):
    """Model For Reviews"""

    user = models.ForeignKey(settings.AUTH_USER_MODEL,
                             on_delete=models.CASCADE)
    ques = models.CharField(max_length=255)
    rating = models.PositiveIntegerField()
    review = models.CharField(max_length=300, blank=True, default="")
    timestamp = models.DateTimeField(auto_now_add=True)

    def __str__(self):
        return f'{self.ques} | {self.user.username}'
