from django.db import models
from django.utils import timezone

QUESTION_TYPES = (
    ('DAILY', 'DAILY'),
    ('WEEKLY', 'WEEKLY'),
    ('MONTHLY', 'MONTHLY')
)


def get_current_time():
    return timezone.now()


class ReviewQuestion(models.Model):
    """Model For Representing Questions To Review"""

    ques = models.CharField(max_length=255)
    check = models.BooleanField(default=False)
    comment = models.CharField(
        max_length=300, blank=True, null=True, default="")
    ques_type = models.CharField(max_length=50, choices=QUESTION_TYPES)
    timestamp = models.DateTimeField(default=get_current_time())

    def __str__(self):
        return f'{self.ques}'
