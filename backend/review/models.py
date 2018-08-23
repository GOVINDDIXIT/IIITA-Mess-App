from django.db import models

QUESTION_TYPES = (
    ('DAILY', 'DAILY'),
    ('WEEKLY', 'WEEKLY'),
    ('MONTHLY', 'MONTHLY')
)


class ReviewQuestion(models.Model):
    """Model For Representing Questions To Review"""

    ques = models.CharField(max_length=255)
    check = models.BooleanField(default=False)
    comment = models.CharField(
        max_length=300, blank=True, null=True, default="")
    ques_type = models.CharField(max_length=50, choices=QUESTION_TYPES)
    timestamp = models.DateTimeField(auto_now_add=True)

    def __str__(self):
        return f'{self.ques}'
