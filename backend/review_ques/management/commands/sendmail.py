import datetime
from django.core.management.base import BaseCommand
from django.core.mail import send_mail

from review_ques.models import ReviewQuestion


class Command(BaseCommand):
    help = 'Send mail if the mess report is not submitted for three consecutive days.'

    def mail(self, days):
        send_mail(
            'Mess Report Not Submitted',
            f'Respected Sir,\nThis is to inform you that the mess report has not been submitted for {days} consecutive days.\nVisit http://vaib79.pythonanywhere.com/ to see the last submitted report.\n\n(Automated Generated Email).',
            'messmanagementiiita@gmail.com',
            ['RECEIPIENT'],
            fail_silently=False
        )

    def add_arguments(self, parser):
        pass

    def handle(self, *args, **options):
        today = datetime.date.today()
        last_submitted_date = ReviewQuestion.objects.all().order_by(
            '-timestamp').first().timestamp.date()
        diff = today - last_submitted_date
        if diff.days >= 3:
            self.mail(diff.days)
