import datetime

from django.utils import timezone
from django.shortcuts import render
from django.http import HttpResponse
from django.core.paginator import Paginator, EmptyPage, PageNotAnInteger
from django.http import JsonResponse
from django.views.decorators.csrf import csrf_exempt

from review.models import ReviewQuestion


def homepage(request):
    """
    View to get all the reports and show them on
    on the homepage of the blog.
    """
    # Flag variable to check whether any reports exits or not.
    status = True
    all_objects = ReviewQuestion.objects.all()

    if not all_objects.exists():
        # Assigning 'status' to False since there are no reports exist.
        status = False

    # set of all dates for which mess reports are present.
    all_dates_set = {object_.timestamp.date() for object_ in all_objects}

    # list of all dates for which mess reports are present.
    all_dates_list = list(all_dates_set)
    all_dates_list.sort(reverse=True)

    reports = []
    for date in all_dates_list:
        queryset = ReviewQuestion.objects.filter(
            timestamp__date=date, ques_type='DAILY')

        # Appending the queryset for the given date in the 'reports' list.
        # This is done to support pagination and also it makes the rendering
        # on the frontend less cumbersome.
        reports.append(queryset)

    # Getting the first page if not passed explicitly.
    page = request.GET.get('page', 1)

    # Posts limit per page.
    paginator = Paginator(reports, 20)

    try:
        posts = paginator.page(page)
    except PageNotAnInteger:
        posts = paginator.page(1)
    except EmptyPage:
        posts = paginator.page(paginator.num_pages)

    return render(request, 'blog/index.html', {'posts': posts, 'status': status})


def view_post(request, date):
    """View to report for a given date"""

    report = ReviewQuestion.objects.filter(timestamp__date=date)

    # Flag variable to check if report for the
    # given date exists or not.
    status = report.exists()
    return render(request, 'blog/view_post.html', {'report': report, 'status': status, 'date': date})


def previous_reports(request):
    """Searching the report of a given date"""

    if request.method == 'POST':
        date = request.POST.get('date')
        queryset = ReviewQuestion.objects.filter(timestamp__date=date)
        return render(request, 'blog/view_post.html', {'report': queryset, 'status': queryset.exists(), 'date': date})
    return render(request, 'blog/previous_report.html', {'today': timezone.now().date})


def developers(request):
    """Developer's Page"""

    return render(request, 'blog/developers.html', {})


@csrf_exempt
def today_report_submitted(request):
    """
    View to check if the report for that day has been
    submitted or not
    """
    queryset = ReviewQuestion.objects.filter(
        timestamp__date=timezone.now().date())
    if not queryset.exists():
        return JsonResponse({'detail': 'Report Not Submitted'}, status=200)
    else:
        return JsonResponse({'detail': 'Report Already Submitted'}, status=403)
