# Generated by Django 5.0.4 on 2024-06-19 22:10

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('api', '0007_spot_address'),
    ]

    operations = [
        migrations.AddField(
            model_name='trip',
            name='spots_associated',
            field=models.ManyToManyField(blank=True, related_name='trips', to='api.spot'),
        ),
    ]
